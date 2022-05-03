package com.mycompany.testtask.screens

import android.content.Context
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.mycompany.testtask.R
import com.mycompany.testtask.models.User
import com.mycompany.testtask.utils.*
import com.mycompany.testtask.viewmodels.UsersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@Composable
fun UserInfoScreen(viewModel: UsersViewModel) {
    val user = viewModel.getCurrentUser().collectAsState()
    val callPermissionState = rememberPermissionState(
        android.Manifest.permission.CALL_PHONE
    )
    val number = cutNumber(user.value.phone ?: "")
    val context = LocalContext.current
    val isCallNumber = remember { mutableStateOf(false) }
    val deviceInfo = rememberDeviceInfo()
    val isTablet = deviceInfo.screenWidthInfo is DeviceInfo.DeviceType.Tablet
    val isPhoneLandScape = deviceInfo.screenHeightInfo is DeviceInfo.DeviceType.MobileLandscape

    val mapView = rememberMapViewWithLifecycle()

    LaunchedEffect(callPermissionState.status !is PermissionStatus.Denied) {
        if (callPermissionState.status !is PermissionStatus.Denied && isCallNumber.value) {
            callNumber(removeSymbolsFromNumber(number), context)
            isCallNumber.value = false
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val modifier = when {
            isTablet -> Modifier.padding(end = 16.dp, top = 16.dp, bottom = 16.dp)
            else -> Modifier.padding(16.dp)
        }
        TopBarTitle(
            stringResource(R.string.user_info),
            isNeedBack = when {
                isTablet -> false
                else -> true
            }
        )
        if (isPhoneLandScape) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        UserInfo(
                            user.value,
                            context,
                            number = number,
                            isCallNumber = isCallNumber,
                            callPermissionState = callPermissionState
                        )
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            MapViewContainer(
                                mapView = mapView,
                                latitude = user.value.address?.geo?.lat ?: "",
                                longitude = user.value.address?.geo?.lng ?: "",
                                address = user.value.address?.street
                                    ?: stringResource(R.string.no_address)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        WebView(url = user.value.website ?: "")
                    }
                }

            }
        } else {
            Column(modifier = modifier.fillMaxSize()) {
                UserInfo(
                    user.value,
                    context,
                    number = number,
                    isCallNumber = isCallNumber,
                    callPermissionState = callPermissionState
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Blue),
                        contentAlignment = Alignment.Center
                    ) {
                        WebView(url = user.value.website ?: "")
                    }
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        MapViewContainer(
                            mapView = mapView,
                            latitude = user.value.address?.geo?.lat ?: "",
                            longitude = user.value.address?.geo?.lng ?: "",
                            address = user.value.address?.street
                                ?: stringResource(R.string.no_address)
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun UserInfo(
    user: User,
    context: Context,
    number: String,
    isCallNumber: MutableState<Boolean>,
    callPermissionState: PermissionState
) {
    val addresses = listOf(user.email ?: "").toTypedArray()
    val emailSubject = "Some title"
    val emailText = "Some message\n\nNew Line\n\n"
    Text(
        text = user.name ?: "",
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = user.email ?: "",
        modifier = Modifier
            .clickable {
                emailCreation(addresses, emailSubject, emailText, context)
            },
        style = TextStyle(
            fontSize = 16.sp
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = number,
        modifier = Modifier.clickable {
            if (callPermissionState.status is PermissionStatus.Denied) {
                callPermissionState.launchPermissionRequest()
                isCallNumber.value = true
            } else {
                callNumber(removeSymbolsFromNumber(number), context)
            }
        },
        style = TextStyle(
            fontSize = 16.sp
        )
    )
}

@Composable
fun WebView(url: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        return false
                    }
                }
            }
        }, update = {
            it.loadUrl("https:$url")
        })
    }
}

@Composable
fun MapViewContainer(
    mapView: MapView,
    latitude: String,
    longitude: String,
    address: String
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White)
    ) {
        AndroidView({ mapView }) { mapView ->
            CoroutineScope(Dispatchers.Main).launch {
                val map = mapView.awaitMap()
                map.clear()
                map.uiSettings.isZoomControlsEnabled = true

                val latLng = getLatLng(latitude, longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                val markerOptions = MarkerOptions()
                    .title(address)
                    .position(latLng)
                map.addMarker(markerOptions)
            }
        }
    }
}
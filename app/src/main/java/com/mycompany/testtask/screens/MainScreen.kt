package com.mycompany.testtask.screens

import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mycompany.testtask.R
import com.mycompany.testtask.models.User
import com.mycompany.testtask.utils.*
import com.mycompany.testtask.viewmodels.UsersViewModel

@ExperimentalPermissionsApi
@Composable
fun MainScreen(
    viewModel: UsersViewModel,
    onUserView: () -> Unit
) {
    val isError = viewModel.getIsError().collectAsState()
    val isLoading = viewModel.getLoading().collectAsState()
    val isFirstBoot = viewModel.getFirstIsBoot().collectAsState()
    val users = viewModel.getUsers().collectAsState()
    val currentUser = viewModel.getCurrentUser().collectAsState()
    val deviceInfo = rememberDeviceInfo()
    val context = LocalContext.current

    val isTablet = deviceInfo.screenWidthInfo is DeviceInfo.DeviceType.Tablet
    val isPhoneLandScape = deviceInfo.screenHeightInfo is DeviceInfo.DeviceType.MobileLandscape

    LaunchedEffect(isError.value) {
        if (isError.value) {
            Toast.makeText(
                context,
                context.resources.getString(R.string.network_loading_error),
                Toast.LENGTH_SHORT
            )
                .show()
            viewModel.setIsError(false)
        }
    }
    LaunchedEffect(isFirstBoot.value) {
        if (isFirstBoot.value) {
            Toast.makeText(
                context,
                context.resources.getString(R.string.data_loading_error),
                Toast.LENGTH_SHORT
            )
                .show()
            viewModel.setIsFirstBoot(false)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }
    LoadingScreen(isLoading.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (isTablet || isPhoneLandScape) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        TopBarTitle(stringResource(R.string.users))
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(users.value.size) {
                                val user = users.value[it]
                                UserItem(
                                    user,
                                    onSelected = {
                                        viewModel.setCurrentUser(user)
                                        onUserView()
                                    },
                                    isTablet = isTablet,
                                    viewModel = viewModel,
                                    isSelected = user.id == currentUser.value.id
                                )
                            }
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        UserInfoScreen(viewModel = viewModel)
                    }
                }
            } else {
                TopBarTitle(stringResource(R.string.users))
                LazyColumn {
                    items(users.value.size) {
                        val user = users.value[it]
                        UserItem(
                            user = user,
                            onSelected = {
                                viewModel.setCurrentUser(user)
                                onUserView()
                            },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopBarTitle(title: String, isNeedBack: Boolean = false) {
    Box(modifier = Modifier.fillMaxWidth()) {
        if (isNeedBack) {
            val dispatcher: OnBackPressedDispatcher =
                LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(12.dp)
                    .clickable {
                        dispatcher.onBackPressed()
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.Black,
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            text = title,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
    }
}

@Composable
fun UserItem(
    user: User,
    onSelected: () -> Unit,
    isTablet: Boolean = false,
    viewModel: UsersViewModel,
    isSelected: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isTablet) {
                    viewModel.setCurrentUser(user)
                } else {
                    onSelected()
                }
            },
        elevation = 6.dp
    ) {
        val modifier = if (isSelected)
            Modifier.background(Color.Black.copy(alpha = 0.1f))
        else Modifier
        Row(modifier = modifier.padding(16.dp)) {
            BaseImage(id = user.id.toString())
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = user.name ?: "",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Text(text = user.email ?: "")
                Spacer(modifier = Modifier.height(70.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = user.company?.catchPhrase ?: "",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun LoadingScreen(
    isLoading: Boolean,
    content: @Composable () -> Unit
) = if (isLoading) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.loading))
            CircularProgressIndicator()
        }
    }
} else {
    content()
}

@Composable
fun BaseImage(id: String) {
    Box(modifier = Modifier.size(90.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(getImageUrl(id))
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_phone_placeholder),
            contentDescription = stringResource(R.string.avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
        )
    }
}
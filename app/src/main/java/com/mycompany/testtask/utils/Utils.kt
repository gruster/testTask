package com.mycompany.testtask.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.mycompany.testtask.R


fun getImageUrl(id: String): String {
    return "${Constants.AVATARS_URL}/$id.jpeg"
}

fun emailCreation(
    addresses: Array<String>, subject: String, text: String, context: Context
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/html"
        putExtra(Intent.EXTRA_EMAIL, addresses)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    try {
        startActivity(
            context,
            Intent.createChooser(intent, context.getString(R.string.send_email)),
            null
        )
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(context, context.getString(R.string.no_email), Toast.LENGTH_SHORT).show()
    }
}

fun callNumber(number: String, context: Context){
    if (number.isNotEmpty()) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:${number}")
            }
            startActivity(context, intent, null)
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.permission), Toast.LENGTH_SHORT).show()
        }
    }
}

fun removeSymbolsFromNumber(number: String): String{
    return number.replace("\\D+".toRegex(), "")
}

fun cutNumber(number: String): String{
    var result = number
    if(number.contains("x")){
        result = number.substring(0, number.indexOf("x") - 1)
    }
    return result
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }

fun getLatLng(lat: String?, lng: String?): LatLng {
    return if(lat == null && lng == null) LatLng(lat?.toDoubleOrNull() ?: 0.0, lng?.toDoubleOrNull() ?: 0.0)
    else LatLng(lat?.toDoubleOrNull() ?: 0.0, lng?.toDoubleOrNull() ?: 0.0)
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}



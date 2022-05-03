package com.mycompany.testtask.screens

import android.content.pm.ActivityInfo
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mycompany.testtask.R
import com.mycompany.testtask.navigation.Screen
import com.mycompany.testtask.utils.DeviceInfo
import com.mycompany.testtask.utils.findActivity
import com.mycompany.testtask.utils.rememberDeviceInfo
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    var startAnimation by remember { mutableStateOf(false)}

    val deviceInfo = rememberDeviceInfo()
    val isTablet = deviceInfo.screenWidthInfo is DeviceInfo.DeviceType.Tablet
    val isPhoneLandScape = deviceInfo.screenHeightInfo is DeviceInfo.DeviceType.MobileLandscape
    val context = LocalContext.current

    val alphaAnim = animateFloatAsState(
        targetValue = if(startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2000
        )
    )
    LaunchedEffect(true){
        if(isTablet && !isPhoneLandScape) {
            val activity = context.findActivity()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        startAnimation = true
        delay(3000)
        navController.popBackStack()
        navController.navigate(Screen.MainScreen.route)
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float){
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = Modifier.alpha(alpha = alpha),
            text = stringResource(R.string.logo),
            color = Color.Black,
            style = TextStyle(fontSize = 60.sp)
        )
    }
}
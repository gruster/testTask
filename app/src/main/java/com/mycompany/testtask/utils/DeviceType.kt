package com.mycompany.testtask.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberDeviceInfo(): DeviceInfo {
    val configuration = LocalConfiguration.current
    return DeviceInfo(
        when{
            configuration.screenWidthDp <= 600 -> DeviceInfo.DeviceType.Mobile
            else -> DeviceInfo.DeviceType.Tablet
        },
        when {
            configuration.screenHeightDp <= 480 -> DeviceInfo.DeviceType.MobileLandscape
            else -> DeviceInfo.DeviceType.Tablet
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}

data class DeviceInfo (
    val screenWidthInfo: DeviceType,
    val screenHeightInfo: DeviceType,
    val screenWidth: Dp,
    val screenHeight: Dp
){
    sealed class DeviceType {
        object Mobile: DeviceType()
        object MobileLandscape: DeviceType()
        object Tablet: DeviceType()
    }
}
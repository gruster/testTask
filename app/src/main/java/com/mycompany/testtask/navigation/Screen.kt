package com.mycompany.testtask.navigation

sealed class Screen(val route: String) {
    object SplashScreen: Screen(route = "splash_screen")
    object MainScreen: Screen(route = "main_screen")
    object UserInfoScreen: Screen(route = "user_info_screen")
}
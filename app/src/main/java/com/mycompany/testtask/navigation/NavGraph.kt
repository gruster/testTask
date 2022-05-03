package com.mycompany.testtask.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mycompany.testtask.screens.MainScreen
import com.mycompany.testtask.screens.SplashScreen
import com.mycompany.testtask.screens.UserInfoScreen
import com.mycompany.testtask.viewmodels.UsersViewModel

@ExperimentalPermissionsApi
@Composable
fun SetupNavGraph(navController: NavHostController,
                  usersViewModel: UsersViewModel
){
    NavHost(navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route){
            SplashScreen(navController = navController)
        }
        composable(Screen.MainScreen.route){
            MainScreen(viewModel = usersViewModel,
                onUserView = { navController.navigate(Screen.UserInfoScreen.route) }
            )
        }
        composable(Screen.UserInfoScreen.route){
            UserInfoScreen(viewModel = usersViewModel)
        }
    }
}
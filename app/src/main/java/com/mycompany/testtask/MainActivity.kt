package com.mycompany.testtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mycompany.testtask.navigation.SetupNavGraph
import com.mycompany.testtask.ui.theme.TestTaskTheme
import com.mycompany.testtask.viewmodels.UsersViewModel

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    private val usersViewModel: UsersViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTaskTheme {
                navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    usersViewModel = usersViewModel
                )
            }
        }
    }
}
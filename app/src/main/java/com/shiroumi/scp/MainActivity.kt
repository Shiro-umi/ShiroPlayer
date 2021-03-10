package com.shiroumi.scp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { MainPage(navController, viewModel) }
                composable("second") { SecondPage(navController) }
                composable("third") { ThirdPage() }
            }
        }
    }
}




//        Handler(mainLooper).postDelayed(
//            { viewModel.apply { autoInitialize() } }, 2000L
//        )
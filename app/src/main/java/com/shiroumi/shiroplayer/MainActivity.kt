package com.shiroumi.shiroplayer

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsetsController.*
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shiroumi.shiroplayer.composable.Home
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeSystemBars()

        setContent {
            NavHost(
                navController = rememberNavController(),
                startDestination = HOME
            ) {
                composable(HOME) { Home() }
            }
        }

        Handler(mainLooper).postDelayed(
            { viewModel.apply { autoInitialize() } }, 2000L
        )
    }

    @Suppress("DEPRECATION")
    private fun initializeSystemBars() {
        window.decorView.apply {
            if (Build.VERSION.SDK_INT >= 30) {
                windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS,
                    APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = Color.rgb(255, 255, 255)
            window.navigationBarColor = Color.TRANSPARENT
        }
    }
}





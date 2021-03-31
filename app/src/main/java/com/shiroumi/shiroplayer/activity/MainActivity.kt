package com.shiroumi.shiroplayer.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shiroumi.shiroplayer.HOME
import com.shiroumi.shiroplayer.arch.activity.BaseActivity
import com.shiroumi.shiroplayer.composable.Home
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeBinderState {
            viewModel.setBinder(it)
            viewModel.updateIndexContent()
        }

        setContent {
            NavHost(
                navController = rememberNavController(),
                startDestination = HOME
            ) {
                composable(HOME) { Home(viewModel = viewModel) }
            }
        }
    }
}




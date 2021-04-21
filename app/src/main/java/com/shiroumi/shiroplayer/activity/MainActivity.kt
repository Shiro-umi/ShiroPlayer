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
import com.shiroumi.shiroplayer.components.PlayMode
import com.shiroumi.shiroplayer.composable.Home
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
@ExperimentalComposeUiApi
class MainActivity : BaseActivity() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeBinderState {
            viewModel.setBinder(it)
            viewModel.updateIndexContent()
            viewModel.selectCurrentMusic()
            // todo 根据保存的设置选择PlayMode
            viewModel.setPlayMode(PlayMode.NORMAL)
        }
        setContent {
            val navCtrl = rememberNavController()
            NavHost(
                navController = navCtrl,
                startDestination = HOME
            ) {
                composable(HOME) {
                    Home(
                        navCtrl = navCtrl,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}




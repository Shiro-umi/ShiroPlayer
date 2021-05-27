package com.shiroumi.shiroplayer.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.ExperimentalComposeUiApi
import com.shiroumi.shiroplayer.arch.activity.BaseActivity
import com.shiroumi.shiroplayer.components.PlayMode
import com.shiroumi.shiroplayer.composable.Home
import com.shiroumi.shiroplayer.viewmodel.PlayerViewModel
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
@ExperimentalComposeUiApi
class MainActivity : BaseActivity() {

    private val viewModel: PlayerViewModel by viewModels()
    private val safLauncher = registerForActivityResult(SafResContract()) { result ->
        Log.wtf("asdasdasd", result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeBinderState {
            viewModel.setBinder(it)
            viewModel.updateIndexContent()
            viewModel.selectCurrentMusic()
            viewModel.setPlayMode(PlayMode.NORMAL)
        }

        setContent {
//            val navCtrl = rememberNavController()
//            NavHost(
//                navController = navCtrl,
//                startDestination = HOME
//            ) {
//                composable(HOME) {
//                    Home(
//                        navCtrl = navCtrl,
//                        viewModel = viewModel
//                    )
//                }
//
//                composable(SETTING) {
//                    // todo setting
//                }
//            }
            Home(viewModel = viewModel)
        }
    }

    fun launchSaf() {
        safLauncher.launch(null)
    }
}




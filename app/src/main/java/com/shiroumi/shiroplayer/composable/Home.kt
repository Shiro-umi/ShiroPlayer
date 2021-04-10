package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
@ExperimentalComposeUiApi
@Composable
fun Home(
    navCtrl: NavHostController,
    viewModel: HomeViewModel
) {
    Column {
        TitleBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
        )

        IndexList(
            viewModel = viewModel
        )
    }
}
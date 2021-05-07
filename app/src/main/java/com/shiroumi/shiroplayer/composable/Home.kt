package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
    Box {
        Column {
            TitleBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.White)
            )

            IndexList(viewModel = viewModel)
        }

        ControlPanel(
            modifier = Modifier.align(Alignment.BottomCenter),
            viewModel = viewModel
        )
    }
}
package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel

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
        )

        IndexList(
            viewModel = viewModel
        )
    }



//    ConstraintLayout(
//        Modifier
//            .fillMaxWidth()
//            .fillMaxHeight()
//            .background(Color.White)
//    ) {
//        val (cTitleBar, cIndexContent, cPlay, cNext) = createRefs()
//
//        TitleBar(
//            Modifier
//                .constrainAs(cTitleBar) {
//                    top.linkTo(parent.top)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                }
//                .fillMaxWidth()
//                .wrapContentHeight()
//        )
//
//        IndexList(
//            navCtrl = navCtrl,
//            viewModel = viewModel,
//            modifier = Modifier
//                .constrainAs(cIndexContent) {
//                    top.linkTo(cTitleBar.bottom)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    bottom.linkTo(parent.bottom)
//                }
//        )
//
//        FloatingActionButton(
//            onClick = { viewModel.play() },
//            modifier = Modifier
//                .constrainAs(cPlay) {
//                    start.linkTo(parent.start)
//                    bottom.linkTo(parent.bottom)
//                }
//                .padding(18.dp, 18.dp, 18.dp, 18.dp)
//                .width(48.dp)
//                .height(48.dp)
//        ) {
//            Text(text = "Play")
//        }
//
//        FloatingActionButton(
//            onClick = { viewModel.playNext() },
//            modifier = Modifier
//                .constrainAs(cNext) {
//                    end.linkTo(parent.end)
//                    bottom.linkTo(parent.bottom)
//                }
//                .padding(18.dp, 18.dp, 18.dp, 18.dp)
//                .width(48.dp)
//                .height(48.dp)
//        ) {
//            Text(text = "Next")
//        }
//    }
}
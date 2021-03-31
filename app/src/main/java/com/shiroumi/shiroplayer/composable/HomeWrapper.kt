package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel
import androidx.compose.runtime.getValue
import com.shiroumi.shiroplayer.Music

@ExperimentalComposeUiApi
@Composable
fun Home(viewModel: HomeViewModel) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        val music: Music by viewModel.music.observeAsState(Music())
        val (cTitleBar, cMusicTitle, cPlay, cNext) = createRefs()
        TitleBar(
            Modifier
                .constrainAs(cTitleBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = music.title,
            Modifier
                .constrainAs(cMusicTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            textAlign = TextAlign.Center
        )
        FloatingActionButton(
            onClick = { viewModel.play() },
            modifier = Modifier
                .constrainAs(cPlay) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .padding(18.dp, 18.dp, 18.dp, 18.dp)
                .width(48.dp)
                .height(48.dp)
        ) {
            Text(text = "Play")
        }
        FloatingActionButton(
            onClick = { viewModel.playNext() },
            modifier = Modifier
                .constrainAs(cNext) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(18.dp, 18.dp, 18.dp, 18.dp)
                .width(48.dp)
                .height(48.dp)
        ) {
            Text(text = "Next")
        }
    }
}

@Composable
fun TitleBar(modifier: Modifier) {
    ConstraintLayout(
        modifier = modifier
            .padding(18.dp, 4.dp, 18.dp, 4.dp)
            .height(48.dp)
    ) {
        val title = createRef()
        Text(
            text = "Compose",
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}
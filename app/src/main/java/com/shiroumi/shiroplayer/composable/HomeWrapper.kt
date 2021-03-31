package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel

@ExperimentalComposeUiApi
@Composable
fun Home(viewModel: HomeViewModel) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
    ) {
        val currentMusic: Music by viewModel.music.observeAsState(Music())
        val indexContent: MutableList<Music> by viewModel.indexContent.observeAsState(mutableListOf())
        val (cTitleBar, cMusicTitle, cIndexContent, cPlay, cNext) = createRefs()
        TitleBar(
            Modifier
                .constrainAs(cTitleBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .wrapContentHeight()
        )
        IndexContentList(
            data = indexContent,
            modifier = Modifier
                .constrainAs(cIndexContent) {
                    top.linkTo(cTitleBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
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
    ) {
        val title = createRef()
        Text(
            text = "Compose",
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(
                    18.dp, 4.dp, 18.dp, 0.dp
                )
                .wrapContentHeight(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}

@Composable
fun IndexContentList(
    data: MutableList<Music>,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(data) { music ->
            IndexItem(music)
        }
    }
}


@Composable
fun IndexItem(music: Music) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = 2.dp
    ) {
        Text(
            text = music.title,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(4.dp, 3.dp)
                .height(48.dp)
        )
    }
}
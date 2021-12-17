package com.shiroumi.shiroplayer.composable.playlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.shiroumi.shiroplayer.activity.MainActivity
import com.shiroumi.shiroplayer.composable.common.Loading
import com.shiroumi.shiroplayer.composable.common.theme.ColorPalette
import com.shiroumi.shiroplayer.composable.common.theme.Orange300
import com.shiroumi.shiroplayer.composable.common.theme.Red300
import com.shiroumi.shiroplayer.composable.common.theme.Theme
import com.shiroumi.shiroplayer.composable.shiroViewModel
import com.shiroumi.shiroplayer.composable.themeViewModel
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.viewmodel.PlayerState


@Composable
fun Playlist(
    modifier: Modifier
) = ConstraintLayout(modifier = modifier) {
    val content by shiroViewModel.mPlaylist.observeAsState()

    AnimatedVisibility(
        visible = content == null,
        exit = fadeOut(),
        enter = fadeIn()
    ) {
        Loading()
    }

    AnimatedVisibility(
        visible = content != null,
        exit = fadeOut(),
        enter = fadeIn()
    ) {
        Playlist(content!!)
    }

    ControlPanel(modifier = controlPanelModifier(createRef()))
}

@Composable
fun Playlist(
    content: List<Music>
) {
    val viewModel = shiroViewModel
    val current by viewModel.currentIndex.observeAsState(Int.MIN_VALUE)
    val playerState by viewModel.currentPlayerState.observeAsState(PlayerState.Stop)
    val listState = rememberLazyListState()

    if (content.isEmpty()) {
        Text(
            text = "媒体库为空",
            modifier = Modifier.fillMaxSize()
        )
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 80.dp)
    ) {
        itemsIndexed(
            items = content,
            key = { index, item ->
                "$index:${item.musicTitle}"
            }
        ) { index, item ->
            ListItem(
                music = item,
                index = index,
                state = if (index == current) CardState.Selected else CardState.UnSelected,
            ) { index, positionInBox ->
                if (index == current) {
                    when (playerState) {
                        PlayerState.Playing -> viewModel.pause()
                        PlayerState.Pause -> viewModel.resume()
                        PlayerState.Stop -> viewModel.play(index)
                        null -> Unit
                    }
                } else {
                    viewModel.play(index)
                }
            }
        }
    }
}

@Composable
fun ProcessBar() {
    val viewModel = shiroViewModel
    val backGroundModifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()

    val cover by viewModel.currentCover.observeAsState(initial = null)

    var progressWidth by remember { mutableStateOf(0f) }

    val progress by viewModel.progress.observeAsState(0f)

    AnimatedVisibility(
        visible = cover != null,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500))
    ) {

        val gravity by themeViewModel.deviceGravity.observeAsState(initial = MainActivity.DeviceGravity())
        val totalWidth = LocalConfiguration.current.screenWidthDp.dp
        val totalHeight = 120.dp
        val pivot by animateOffsetAsState(
            targetValue = Offset(
                0.022222222f * gravity.x,
                0.022222222f * gravity.y
            ),
            animationSpec = tween(500, easing = LinearEasing)
        )

        Image(
            modifier = backGroundModifier
                .scale(1.4f)
                .offset(-(totalWidth / 1.4f * pivot.x), (totalHeight / 1.4f * pivot.y)),
            bitmap = cover!!.asImageBitmap(),
            contentDescription = "cover",
            contentScale = ContentScale.FillWidth,
            alpha = 1f
        )
    }

    Box(
        modifier = backGroundModifier
            .background(
                Brush.horizontalGradient(
                    listOf(ColorPalette.main, Color.Transparent)
                )
            )
            .onGloballyPositioned {
                progressWidth = it.size.width.toFloat()
            }
            .scrollable(
                orientation = Orientation.Horizontal,
                state = rememberScrollableState { delta ->
                    viewModel.pendingSeek = true
                    val newProgress = progress + delta / progressWidth / 2
                    if ((0f..1f).contains(newProgress)) {
                        viewModel.seekLocal(newProgress)
                    }
                    newProgress * progressWidth
                },
                flingBehavior = object : FlingBehavior {
                    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
                        viewModel.seekRemote()
                        return 0f
                    }
                }
            )
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(
                        when (themeViewModel.currentTheme) {
                            Theme.Light -> Orange300
                            Theme.Dark -> Red300
                        }
                    )
                    .align(Alignment.CenterEnd)
            ) {}
        }
    }
}


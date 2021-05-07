package com.shiroumi.shiroplayer.composable

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.util.toRange
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel
import com.shiroumi.shiroplayer.viewmodel.PlayerState
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.*

@ExperimentalContracts
@Composable
fun IndexList(
    viewModel: HomeViewModel
) {
    val indexContent: MutableList<Music>
            by viewModel.playList.observeAsState(mutableListOf())

    val selected by viewModel.musicIndex.observeAsState(-1)
    var offset by remember { mutableStateOf(IntOffset(0, 0)) }

    CustomBox(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        loadingAnimVerticalOffset = 42,
        loadingCase = { indexContent.size == 0 }
    ) {
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState
        ) {
            itemsIndexed(
                items = indexContent,
                key = { index, item ->
                    "$index:${item._id}"
                }
            ) { index, item ->
                ListItem(
                    music = item,
                    index = index,
                    viewModel = viewModel,
                    state = if (index == selected) CardState.Selected else CardState.UnSelected
                ) { i, positionInBox ->
                    with(viewModel) {
                        if (i == selected) {
                            when (playerState.value) {
                                PlayerState.PLAYING -> pause.withClickFilter(100L)
                                PlayerState.PAUSE -> resume.withClickFilter(100L)
                                else -> pause.withClickFilter(100L)
                            }
                            return@ListItem
                        }
                        play.withClickFilter(index, 200L) {
                            clearCoverNow()
                            resetProcessNow()
                        }
                        musicIndex.value = i
                        offset = positionInBox
                    }
                }
            }
        }
        // TODO expand item

    }
}

@ExperimentalContracts
@Composable
fun ListItem(
    music: Music,
    index: Int,
    state: CardState,
    viewModel: HomeViewModel,
    selectListener: (Int, IntOffset) -> Unit
) {
    val elevation by animateFloatAsState(
        targetValue = if (state == CardState.UnSelected) 0f else 4f,
        animationSpec = spring(Spring.DampingRatioNoBouncy, Spring.StiffnessHigh)
    )

    SelectableItem(
        selectListener = { thisPosition ->
            selectListener.invoke(index, thisPosition)
        }
    ) { modifier ->
        Card(
            modifier = modifier
                .zIndex(if (state == CardState.UnSelected) 0f else 1f),
            shape = RoundedCornerShape(0.dp),
            elevation = elevation.dp
        ) {
            Box {
                val cover = viewModel.musicCover.observeAsState().value
                val gradientEnd by animateColorAsState(
                    targetValue = if (cover == null) Color.White else Color.Transparent,
                    animationSpec = spring(Spring.DampingRatioNoBouncy, Spring.StiffnessVeryLow)
                )

                if (state == CardState.Selected && cover.exist()) {
                    ProcessBar(
                        viewModel = viewModel,
                        cover = cover,
                        gradientEnd = gradientEnd
                    )
                }

                Column(
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp, 8.dp, 8.dp, 0.dp)
                            .wrapContentHeight(),
                        text = music.title,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier
                            .padding(16.dp, 8.dp, 8.dp, 0.dp),
                        text = "${music.artist} - ${music.album}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
fun ProcessBar(
    viewModel: HomeViewModel,
    cover: Bitmap,
    gradientEnd: Color
) {
    val backGroundModifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
    Image(
        modifier = backGroundModifier,
        bitmap = cover.asImageBitmap(),
        contentDescription = "cover",
        contentScale = ContentScale.FillWidth,
        alpha = 1f,
    )

    var progressWidth by remember { mutableStateOf(0f) }
    val process = viewModel.playingProcess.observeAsState(0f).value
    Box(
        modifier = backGroundModifier
            .background(
                Brush.horizontalGradient(
                    listOf(Color.White, gradientEnd)
                )
            )
            .onGloballyPositioned {
                progressWidth = it.size.width.toFloat()
            }
            .scrollable(
                orientation = Orientation.Horizontal,
                state = rememberScrollableState { delta ->
                    val newProgress = process + delta / progressWidth / 2
                    if ((0f..1f)
                            .toRange()
                            .contains(newProgress)
                    ) {
                        viewModel.localSeekTo(newProgress)
                    }
                    newProgress * progressWidth
                }
            )
    ) {
        Box(
            Modifier
                .background(Color.White)
                .fillMaxHeight()
                .fillMaxWidth(process)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color(0xFFEEEEEE))
                    .align(Alignment.CenterEnd)
            ) {}
        }
    }
}

@Composable
fun SelectableItem(
    selectListener: (IntOffset) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    var thisPosition by remember { mutableStateOf(IntOffset(0, 0)) }
    content(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .rippleClickable {
                selectListener(thisPosition)
            }
            .onGloballyPositioned {
                val position = it.positionInParent()
                thisPosition = IntOffset(position.x.roundToInt(), position.y.roundToInt())
            }
    )
}

enum class CardState {
    UnSelected,
    Selected
}

@ExperimentalContracts
fun Bitmap?.exist(): Boolean {
    contract {
        returns(true) implies (this@exist != null)
    }
    return this != null
}
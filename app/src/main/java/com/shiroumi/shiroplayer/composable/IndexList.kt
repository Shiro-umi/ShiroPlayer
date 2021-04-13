package com.shiroumi.shiroplayer.composable

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.roundToInt

@ExperimentalContracts
@Composable
fun IndexList(
    viewModel: HomeViewModel
) {
    val indexContent: MutableList<Music>
            by viewModel.playList.observeAsState(mutableListOf())

    val selected by viewModel.musicIndex.observeAsState(-1)
    var reSelected by rememberSimpleSavable(value = false)
    var title by rememberSimpleSavable(value = "")
    var offset by remember { mutableStateOf(IntOffset(0, 0)) }

    Box {
        LazyColumn {
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
                    viewModel.apply {
                        if (i == selected) {
                            when (viewModel.musicState) {
                                HomeViewModel.MusicState.PLAYING -> pause.withClickFilter(100L)
                                HomeViewModel.MusicState.PAUSE -> resume.withClickFilter(100L)
                                else -> pause.withClickFilter(100L)
                            }
                            return@ListItem
                        }
                        play.withClickFilter(index, 200L) {
                            resetProcessNow()
                            clearCoverNow()
                        }
                        viewModel.musicIndex.value = i
                        offset = positionInBox
                    }
                }
            }
        }
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

    Box(
        modifier = backGroundModifier
            .background(
                Brush.horizontalGradient(
                    listOf(Color.White, gradientEnd)
                )
            )
    ) {
        val process = viewModel.playingProcess.observeAsState(0f).value
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
    val interactionSource = remember { MutableInteractionSource() }
    var thisPosition by remember { mutableStateOf(IntOffset(0, 0)) }
    content(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(
                enabled = true,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple()
            ) {
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
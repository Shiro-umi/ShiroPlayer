package com.shiroumi.shiroplayer.composable

import android.graphics.Bitmap
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.shiroumi.shiroplayer.Music
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel
import kotlin.math.roundToInt

@Composable
fun IndexList(
    viewModel: HomeViewModel
) {
    val indexContent: MutableList<Music>
            by viewModel.playList.observeAsState(mutableListOf())

    var selected by rememberSimpleSavable(value = -1)
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
                    if (i == selected) {
                        reSelected = true
                        return@ListItem
                    }
                    reSelected = false
                    selected = i
                    offset = positionInBox
                }
            }
        }

    }
}

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
            viewModel.play(index)
        }
    ) { modifier ->
        Card(
            modifier = modifier
                .zIndex(if (state == CardState.UnSelected) 0f else 1f),
            shape = RoundedCornerShape(0.dp),
            elevation = elevation.dp
        ) {
            Box {
                val cover: Bitmap? by viewModel.musicCover.observeAsState()
                cover?.apply {
                    if (state == CardState.Selected) {
                        Image(bitmap = this.asImageBitmap(), contentDescription = "cover")
                    }
                }
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(0.dp, 0.dp, 8.dp, 0.dp),
                    text = music.title
                )
            }
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
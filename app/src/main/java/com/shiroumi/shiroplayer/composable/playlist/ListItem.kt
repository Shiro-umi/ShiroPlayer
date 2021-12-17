package com.shiroumi.shiroplayer.composable.playlist

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.shiroumi.shiroplayer.composable.common.theme.ColorPalette
import com.shiroumi.shiroplayer.composable.common.theme.ColorPaletteImpl
import com.shiroumi.shiroplayer.composable.common.theme.Theme
import com.shiroumi.shiroplayer.composable.themeViewModel
import com.shiroumi.shiroplayer.room.entities.Music
import com.shiroumi.shiroplayer.viewmodel.animateColorAsState

@Composable
fun ListItem(
    music: Music,
    index: Int,
    state: CardState,
    selectListener: (position: Int, offset: IntOffset) -> Unit
) {

    val selectableItemState by rememberSelectableItemState(
        cardState = state,
        theme = themeViewModel.currentTheme
    )

    SelectableItem(
        selectableItemState = selectableItemState,
        selectListener = { thisPosition ->
            selectListener.invoke(index, thisPosition)
        }
    ) {
        Box {
            if (state == CardState.Selected) {
                ProcessBar()
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
                    text = music.musicTitle,
                    fontSize = 18.sp,
                    color = ColorPalette.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp, 8.dp, 8.dp, 0.dp),
                    text = "${music.artist} - ${music.album}",
                    fontSize = 11.sp,
                    color = when (themeViewModel.currentTheme) {
                        Theme.Dark -> ColorPalette.darkerText
                        Theme.Light -> ColorPalette.lighterText
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

sealed class CardState {
    object UnSelected : CardState()
    object Selected : CardState()
}
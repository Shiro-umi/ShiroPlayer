package com.shiroumi.shiroplayer.composable.playlist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.shiroumi.shiroplayer.composable.common.rippleClickable
import com.shiroumi.shiroplayer.composable.common.theme.ColorPalette
import com.shiroumi.shiroplayer.composable.common.theme.ColorPaletteImpl
import com.shiroumi.shiroplayer.composable.common.theme.Theme
import com.shiroumi.shiroplayer.composable.themeViewModel
import com.shiroumi.shiroplayer.viewmodel.animateColorAsState
import kotlin.math.roundToInt

@Composable
fun SelectableItem(
    selectableItemState: SelectableItemState,
    selectListener: (IntOffset) -> Unit,
    content: @Composable () -> Unit
) {
    var thisPosition by remember { mutableStateOf(IntOffset(0, 0)) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(selectableItemState.cardHeight)
            .rippleClickable(
                color = when (themeViewModel.currentTheme) {
                    Theme.Light -> Color.Unspecified
                    Theme.Dark -> ColorPalette.lighterText
                }
            ) {
                selectListener(thisPosition)
            }
            .onGloballyPositioned {
                val position = it.positionInParent()
                thisPosition = IntOffset(position.x.roundToInt(), position.y.roundToInt())
            }
            .zIndex(
                when (selectableItemState.cardState) {
                    CardState.UnSelected -> 0f
                    CardState.Selected -> 1f
                }
            ),
        backgroundColor = selectableItemState.backgroundColor,
        shape = RoundedCornerShape(0.dp),
        elevation = selectableItemState.elevation
    ) {
        content()
    }
}


@Composable
fun rememberSelectableItemState(
    cardState: CardState,
    theme: Theme,
): State<SelectableItemState> {
    val colorPalette = ColorPalette

    val color by rememberUpdatedState(
        newValue = mapToColor(
            state = cardState,
            theme = theme,
            colorPalette = colorPalette
        )
    )

    val state = remember {
        mutableStateOf(
            SelectableItemState(cardState = cardState, backgroundColor = color)
        )
    }

    val backgroundColor by animateColorAsState(
        key = "PlaylistItem",
        targetValue = color,
        animationSpec = tween(600),
        defaultColor = color
    )

    val cardHeight by animateDpAsState(
        targetValue = when (cardState) {
            CardState.UnSelected -> 64.dp
            CardState.Selected -> 120.dp
        }
    )

    state.value = SelectableItemState(
        cardState = cardState,
        cardHeight = cardHeight,
        elevation = when (cardState) {
            CardState.UnSelected -> 0.dp
            CardState.Selected -> 4.dp
        },
        backgroundColor = backgroundColor
    )

    return state
}

class SelectableItemState(
    var cardState: CardState,
    var cardHeight: Dp = 64.dp,
    var elevation: Dp = 0.dp,
    var backgroundColor: Color
)

private fun mapToColor(
    state: CardState,
    theme: Theme,
    colorPalette: ColorPaletteImpl
) = when (state) {
    CardState.Selected -> when (theme) {
        Theme.Light -> colorPalette.lighterMain
        Theme.Dark -> colorPalette.lighterMain
    }
    CardState.UnSelected -> colorPalette.main
}

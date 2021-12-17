package com.shiroumi.shiroplayer.composable.playlist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import com.shiroumi.shiroplayer.composable.common.theme.ColorPalette
import com.shiroumi.shiroplayer.composable.common.rippleClickable
import com.shiroumi.shiroplayer.composable.shiroViewModel
import com.shiroumi.shiroplayer.viewmodel.PlayerState

@Composable
fun ControlPanel(
    modifier: Modifier
) = Card(
    modifier = modifier,
    shape = RoundedCornerShape(8.dp),
    backgroundColor = ColorPalette.lighterMain,
    elevation = 8.dp
) {
    val shiroViewModel = shiroViewModel
    val playerState by shiroViewModel.currentPlayerState.observeAsState(PlayerState.Stop)
    val currentIndex by shiroViewModel.currentIndex.observeAsState(initial = 0)

    BuildPanel(listOf(
        PanelButtonType.TYPE_PLAY_PREV to { shiroViewModel.playPrev() },
        if (playerState is PlayerState.Playing) {
            PanelButtonType.TYPE_PAUSE
        } else {
            PanelButtonType.TYPE_PLAY
        } to {
            when (playerState) {
                PlayerState.Stop -> shiroViewModel.play(currentIndex)
                PlayerState.Pause -> shiroViewModel.resume()
                PlayerState.Playing -> shiroViewModel.pause()
                else -> Log.wtf("control panel", "unexpected playerState case")
            }
            Unit
        },
        PanelButtonType.TYPE_PLAY_NEXT to { shiroViewModel.playNext() },
        PanelButtonType.TYPE_STOP to { shiroViewModel.stop() }
    ))

}

@Composable
private fun BuildPanel(
    list: List<Pair<PanelButtonType, () -> Unit>>
) = Row {
    list.forEach { config ->
        PanelButton(type = config.first, config.second)
    }
}

@Composable
private fun RowScope.PanelButton(
    type: PanelButtonType,
    behavior: () -> Unit
) = Box(
    modifier = panelButtonModifier(behavior = behavior),
    content = { PanelButtonIcon(type) }
)

@Composable
private fun BoxScope.PanelButtonIcon(
    type: PanelButtonType
) = Icon(
    modifier = Modifier.align(Alignment.Center),
    imageVector = type.source,
    contentDescription = type.contentDescription,
    tint = ColorPalette.text
)


fun RowScope.panelButtonModifier(
    behavior: () -> Unit
) = Modifier
    .fillMaxHeight()
    .weight(weight = 1f, fill = true)
    .rippleClickable(behavior = behavior)

private enum class PanelButtonType(
    val source: ImageVector,
    val contentDescription: String
) {
    TYPE_PLAY_PREV(
        source = Icons.Rounded.SkipPrevious,
        contentDescription = "Play Prev"
    ),
    TYPE_PLAY(
        source = Icons.Rounded.PlayArrow,
        contentDescription = "Play"
    ),
    TYPE_PLAY_NEXT(
        source = Icons.Rounded.SkipNext,
        contentDescription = "Play Next"
    ),
    TYPE_PAUSE(
        source = Icons.Rounded.Pause,
        contentDescription = "Pause"
    ),
    TYPE_STOP(
        source = Icons.Rounded.Stop,
        contentDescription = "Stop"
    )
}

@Preview
@Composable
fun ControlPanelPreview() = ConstraintLayout(
    modifier = Modifier.fillMaxSize()
) {
    ControlPanel(
        modifier = controlPanelModifier(createRef())
    )
}

@SuppressLint("ModifierFactoryExtensionFunction")
fun ConstraintLayoutScope.controlPanelModifier(ref: ConstrainedLayoutReference) = Modifier
    .height(54.dp)
    .width(240.dp)
    .constrainAs(ref) {
        linkTo(start = parent.start, end = parent.end)
        bottom.linkTo(anchor = parent.bottom, margin = 24.dp)
    }
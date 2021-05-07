package com.shiroumi.shiroplayer.composable

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.shiroumi.shiroplayer.viewmodel.HomeViewModel
import com.shiroumi.shiroplayer.viewmodel.PlayerState

private val panelHeight = 54.dp
private val buttonWidth = 60.dp

@Composable
fun ControlPanel(
    viewModel: HomeViewModel,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .padding(0.dp, 0.dp, 0.dp, 24.dp)
            .fillMaxWidth()
            .height(panelHeight)
    ) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .zIndex(10f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
            ) {
                with(viewModel) {
                    val playerState by playerState.observeAsState(PlayerState.STOP)

                    PanelButton(PanelButtonType.TYPE_PLAY_PREV) {

                    }

                    PanelButton(
                        if (playerState != PlayerState.PLAYING) {
                            PanelButtonType.TYPE_PLAY
                        } else {
                            PanelButtonType.TYPE_PAUSE
                        }
                    ) {
                        when (playerState) {
                            PlayerState.STOP -> play(currentIndex)
                            PlayerState.PAUSE -> resume()
                            PlayerState.PLAYING -> pause()
                            else -> Log.wtf("control panel", "unexpected playerState case")
                        }
                    }
                    PanelButton(PanelButtonType.TYPE_PLAY_NEXT) {
                        playNext()
                    }
                }
            }
        }
    }
}

@Composable
private fun PanelButton(
    type: PanelButtonType,
    behavior: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(buttonWidth)
            .rippleClickable(behavior)
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = type.source,
            contentDescription = type.contentDescription,
            tint = Color(0xFF666666)
        )
    }
}

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
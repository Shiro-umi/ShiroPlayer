package com.shiroumi.shiroplayer.composable.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.shiroumi.shiroplayer.composable.common.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DefaultRf = 15f
private const val DefaultRb = 35f
private const val MaxRf = 55f
private const val MaxRb = 65f

@Composable
fun Loading(
    centerVerticalOffset: Int = 0
) {

    var fState: FState by remember {
        mutableStateOf(FState.Close)
    }

    var bState: BState by remember {
        mutableStateOf(BState.Close)
    }

    var rf by remember {
        mutableStateOf(DefaultRf)
    }

    var rb by remember {
        mutableStateOf(DefaultRb)
    }

    var colorNode by remember {
        mutableStateOf(listOf(Red300, Orange300).toCircleLinkedList())
    }

    val fColor by animateColorAsState(
        targetValue = colorNode?.data ?: Grey300,
        animationSpec = tween(durationMillis = 500)
    )

    val bColor by rememberUpdatedState(newValue = colorNode?.prev?.data)

    LaunchedEffect(fState) {
        when (fState) {
            FState.Close -> animate(
                initialValue = MaxRf,
                targetValue = DefaultRf,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = CubicBezierEasing(.85f, .01f, .58f, .98f)
                )
            ) { value, _ ->
                rf = value
            }
            FState.Expand -> animate(
                initialValue = DefaultRf,
                targetValue = MaxRf,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = CubicBezierEasing(.24f, .65f, .37f, .99f)
                )
            ) { value, _ ->
                rf = value
                if (value >= DefaultRb) {
                    bState = BState.Expand
                }
                if (value.i == MaxRf.i) {
                    fState = FState.Close
                }
            }
        }
    }

    LaunchedEffect(bState) {
        when (bState) {
            BState.Close -> {
                animate(
                    initialValue = MaxRb,
                    targetValue = DefaultRb,
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(.85f, .01f, .58f, .98f)
                    )
                ) { value, _ ->
                    rb = value
                    if (value.i == DefaultRb.i) {
                        launch {
                            delay(300L)
                            fState = FState.Expand
                        }
                    }
                }
            }
            BState.Expand -> {
                colorNode = colorNode?.next
                animate(
                    initialValue = DefaultRb,
                    targetValue = MaxRb,
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(.24f, .65f, .37f, .99f)
                    )
                ) { value, _ ->
                    rb = value
                    if (value.i == MaxRb.i) {
                        bState = BState.Close
                    }
                }
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = bColor ?: Grey300,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2 - centerVerticalOffset),
            radius = rb
        )
        drawCircle(
            color = fColor,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2 - centerVerticalOffset),
            radius = rf
        )
    }
}

sealed class FState {
    object Expand : FState()
    object Close : FState()
}

sealed class BState {
    object Expand : BState()
    object Close : BState()
}

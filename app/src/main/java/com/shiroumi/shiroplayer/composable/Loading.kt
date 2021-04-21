package com.shiroumi.shiroplayer.composable

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun Loading(
    centerVerticalOffset: Int = 0
) {
    val radius by rememberInfiniteTransition().animateFloat(
        initialValue = 10f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = Color.LightGray,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2 - centerVerticalOffset),
            radius = radius,
            style = Stroke(
                width = radius * (radius / 100)
            )
        )
    }
}
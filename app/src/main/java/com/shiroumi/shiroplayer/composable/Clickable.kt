package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

fun Modifier.rippleClickable(
    behavior: () -> Unit
) = composed {
    clickable(
        enabled = true,
        role = Role.Button,
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple()
    ) {
        behavior()
    }
}
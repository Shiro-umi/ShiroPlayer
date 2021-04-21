package com.shiroumi.shiroplayer.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomBox(
    modifier: Modifier,
    loadingAnimVerticalOffset: Int,
    loadingCase: (() -> Boolean)?,
    block: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
    ) {
        if (loadingCase != null && loadingCase()) {
            Loading(loadingAnimVerticalOffset)
        } else {
            block()
        }
    }
}
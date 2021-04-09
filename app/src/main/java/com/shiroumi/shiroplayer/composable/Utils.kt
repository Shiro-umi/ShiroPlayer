package com.shiroumi.shiroplayer.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun <T> rememberSimpleSavable(value: T): MutableState<T> {
    return rememberSaveable(stateSaver = autoSaver()) {
        mutableStateOf(value)
    }
}
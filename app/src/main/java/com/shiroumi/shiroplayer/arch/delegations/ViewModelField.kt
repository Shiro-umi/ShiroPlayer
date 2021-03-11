package com.shiroumi.shiroplayer.arch.delegations

import androidx.lifecycle.SavedStateHandle

class ViewModelField<T>(
    private val savedStateHandle: SavedStateHandle,
    private val tag: String
) {
    var value: T? = null
        set(value) {
            savedStateHandle[tag] = value
            field = value
        }

}
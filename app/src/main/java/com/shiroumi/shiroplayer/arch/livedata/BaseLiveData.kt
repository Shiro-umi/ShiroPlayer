package com.shiroumi.shiroplayer.arch.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

class BaseLiveData<T>(
    private val tag: String,
    private val savedStateHandle: SavedStateHandle
) : MutableLiveData<T>() {
    override fun setValue(value: T?) {
        value?.let {
            savedStateHandle[tag] = value
            super.setValue(value)
        }
    }
}
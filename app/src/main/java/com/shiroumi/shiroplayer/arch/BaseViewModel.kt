package com.shiroumi.shiroplayer.arch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.shiroplayer.KEY_AUTO_INITIALIZED
import com.shiroumi.shiroplayer.arch.delegations.ViewModelStateDelegation

abstract class BaseViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val viewModelStateDelegation = ViewModelStateDelegation(savedStateHandle)

    private var autoInitialized: Boolean? = savedStateHandle.get<Boolean>(KEY_AUTO_INITIALIZED)

    fun autoInitialize() {
        if (autoInitialized == true) return
        initialize()
        savedStateHandle.set(KEY_AUTO_INITIALIZED, true)
        autoInitialized = true
    }

    abstract fun initialize()
}
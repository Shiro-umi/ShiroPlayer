package com.shiroumi.scp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.scp.delegations.ViewModelStateDelegation

open class BaseViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _viewModelStateDelegation = ViewModelStateDelegation(savedStateHandle)
    val viewModelStateDelegation: ViewModelStateDelegation
        get() = _viewModelStateDelegation

    private var autoInitialized: Boolean? = savedStateHandle.get<Boolean>(KEY_AUTO_INITIALIZED)

    fun autoInitialize(block: () -> Unit) {
        if (autoInitialized == true) return
        block.invoke()
        savedStateHandle.set(KEY_AUTO_INITIALIZED, true)
        autoInitialized = true
    }


}
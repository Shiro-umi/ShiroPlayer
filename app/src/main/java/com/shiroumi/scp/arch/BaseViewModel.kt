package com.shiroumi.scp.arch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.scp.KEY_AUTO_INITIALIZED
import com.shiroumi.scp.arch.delegations.ViewModelStateDelegation

open class BaseViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val viewModelStateDelegation = ViewModelStateDelegation(savedStateHandle)

    private var autoInitialized: Boolean? = savedStateHandle.get<Boolean>(KEY_AUTO_INITIALIZED)

    fun autoInitialize(block: () -> Unit) {
        if (autoInitialized == true) return
        block.invoke()
        savedStateHandle.set(KEY_AUTO_INITIALIZED, true)
        autoInitialized = true
    }


}
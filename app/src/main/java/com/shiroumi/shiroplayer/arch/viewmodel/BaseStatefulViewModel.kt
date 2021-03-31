package com.shiroumi.shiroplayer.arch.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.shiroplayer.arch.delegations.ViewModelStateDelegation
import com.shiroumi.shiroplayer.viewmodel.KEY_INITIALIZED

open class BaseStatefulViewModel(
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val stateDelegation = ViewModelStateDelegation(savedStateHandle)

    var initialized: Boolean? = savedStateHandle.get<Boolean>(KEY_INITIALIZED)

    inline fun <reified T> initialize(block: (T) -> Unit = {}) {
        if (initialized == true) return
        block(this as T)
        savedStateHandle.set(KEY_INITIALIZED, true)
        initialized = true
    }

//    val title: MutableLiveData<String> by stateDelegation.byLiveData("main_view_model_title")
//    val document: ViewModelField<Document?> by stateDelegation.byField("main_view_model_document",null)
}
package com.shiroumi.shiroplayer.arch.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.shiroplayer.IMusicService
import com.shiroumi.shiroplayer.arch.delegations.ViewModelStateDelegation
import com.shiroumi.shiroplayer.viewmodel.KEY_INITIALIZED

open class BaseStatefulViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val stateDelegation = ViewModelStateDelegation(savedStateHandle)

//    fun setBinder(musicService: IMusicService, block: () -> Unit) {
//        block()
//    }

//    val title: MutableLiveData<String> by stateDelegation.byLiveData("main_view_model_title")
//    val document: ViewModelField<Document?> by stateDelegation.byField("main_view_model_document",null)
}
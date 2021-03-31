package com.shiroumi.shiroplayer.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.shiroumi.shiroplayer.arch.viewmodel.BaseStatefulViewModel

const val TAG_MAIN_VIEW_MODEL = "log:main_view_model"
const val KEY_INITIALIZED = "key:initialized"

class MainViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle
) : BaseStatefulViewModel(savedStateHandle) {
//    val title: MutableLiveData<String> by viewModelStateDelegation.byLiveData("main_view_model_title")
//    val document: ViewModelField<Document?> by viewModelStateDelegation.byField(
//        "main_view_model_document",
//        null
//    )
}

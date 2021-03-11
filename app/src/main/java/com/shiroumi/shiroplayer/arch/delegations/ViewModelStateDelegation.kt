package com.shiroumi.shiroplayer.arch.delegations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

const val STATUS_LIVE_DATA = "saved_state:live_data_repo"
const val STATUS_FIELD = "saved_state:field"

class ViewModelStateDelegation(
    private val savedStateHandle: SavedStateHandle,
    liveDataRepo: HashMap<String, MutableLiveData<*>> = HashMap(),
    fieldRepo: HashMap<String, Any> = HashMap()
) {
    init {
        savedStateHandle.set(STATUS_LIVE_DATA, liveDataRepo)
        savedStateHandle.set(STATUS_FIELD, fieldRepo)
    }

    fun <T> byLiveData(tag: String): LiveDataDelegation<T> {
        return LiveDataDelegation(savedStateHandle, tag)
    }

    fun <T> byField(tag: String, factory: (() -> T)?): FieldDelegation<T> {
        return FieldDelegation(savedStateHandle, tag, factory)
    }
}



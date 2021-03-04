package com.shiroumi.scp.arch.delegations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle

const val STATUS_LIVE_DATA = "saved_state:live_data_repo"

class ViewModelStateDelegation(
    private val savedStateHandle: SavedStateHandle,
    liveDataRepo: HashMap<String, MutableLiveData<*>> = HashMap()
) {
    init {
        savedStateHandle.set(STATUS_LIVE_DATA, liveDataRepo)
    }

    fun <T> byLiveData(tag: String): LiveDataDelegation<T> {
        return LiveDataDelegation(savedStateHandle, tag)
    }
}



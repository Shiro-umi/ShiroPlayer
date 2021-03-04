package com.shiroumi.scp.arch.delegations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.scp.arch.BaseLiveData
import kotlin.reflect.KProperty

class LiveDataDelegation<T> constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tag: String
) {
    private val value: MutableLiveData<T> = BaseLiveData(tag, savedStateHandle)
        get() {
            field.value = savedStateHandle[tag]
            return field
        }

    operator fun getValue(
        thisRef: ViewModel,
        property: KProperty<*>
    ): MutableLiveData<T> {
        return value
    }

}
package com.shiroumi.scp.arch.delegations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.shiroumi.scp.arch.BaseLiveData
import com.shiroumi.scp.arch.BaseViewModel
import kotlin.reflect.KProperty

class LiveDataDelegation<T> constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tag: String
) : AbstractViewModelFieldDelegation<MutableLiveData<T>>() {
    override val value: MutableLiveData<T> = BaseLiveData(tag, savedStateHandle)
        get() {
            field.value = savedStateHandle[tag]
            return field
        }

    override operator fun getValue(
        thisRef: BaseViewModel,
        property: KProperty<*>
    ): MutableLiveData<T> {
        return value
    }
}
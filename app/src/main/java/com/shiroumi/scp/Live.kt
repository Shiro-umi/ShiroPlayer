package com.shiroumi.scp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.reflect.KProperty

class Live<U> constructor(private val savedStateHandle: SavedStateHandle) {
    var value: MutableLiveData<U> = MutableLiveData<U>()
        get() {
            val saved = savedStateHandle.get<MutableLiveData<U>>(SAVED_INDEX_PAGE)
            return if (field.value != null) {
                field
            } else {
                field.value = saved?.value
                field
            }
        }
        set(value) {
            field = value
            savedStateHandle.set(SAVED_INDEX_PAGE, value)
        }

    operator fun getValue(
        thisRef: ViewModel,
        property: KProperty<*>
    ): MutableLiveData<U> {
        return value
    }
}
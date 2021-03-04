package com.shiroumi.scp.delegations

import android.util.Log
import android.util.SparseArray
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.scp.BaseLiveData
import com.shiroumi.scp.TAG_MAIN_VIEW_MODEL
import kotlin.reflect.KProperty

const val STATUS_LIVE_DATA = "saved_state:live_data_repo"

class ViewModelStateDelegation(
    private val savedStateHandle: SavedStateHandle
) {
    private val liveDataRepo: HashMap<String, MutableLiveData<*>> = HashMap()

    init {
        savedStateHandle.set(STATUS_LIVE_DATA, liveDataRepo)
    }

    fun <T> byLiveData(tag: String): LiveDataDelegation<T> {
        return LiveDataDelegation(savedStateHandle, tag, null)
    }

    fun <T> byLiveData(tag: String, default: () -> T): LiveDataDelegation<T> {
        return LiveDataDelegation(savedStateHandle, tag, default)
    }
}

class LiveDataDelegation<T> constructor(
    savedStateHandle: SavedStateHandle,
    private val tag: String,
    private val default: (() -> T)?
) {
    private val liveDataRepo =
        savedStateHandle.get<HashMap<String, BaseLiveData<T>>>(STATUS_LIVE_DATA)
    private var _value = BaseLiveData(this)
    private var value: BaseLiveData<T>
        get() {
            return if (_value.value != null) {
                _value
            } else {
                liveDataRepo?.let {
                    it[tag]?.apply {
                        _value.setValue(value)
                    } ?: {
                        _value.value = default?.invoke()
                    }
                }
                _value
            }
        }
        set(value) {
            _value = value
        }

    operator fun getValue(
        thisRef: ViewModel,
        property: KProperty<*>
    ): MutableLiveData<T> {
        return value
    }

    fun store() {
        liveDataRepo?.put(tag, _value)
    }
}

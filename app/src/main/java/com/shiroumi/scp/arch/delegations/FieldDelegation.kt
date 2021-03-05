package com.shiroumi.scp.arch.delegations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.scp.arch.BaseViewModel
import kotlin.reflect.KProperty

class FieldDelegation<T> constructor(
    private val savedStateHandle: SavedStateHandle,
    private val tag: String,
    private val factory: (() -> T)?
) : AbstractViewModelFieldDelegation<ViewModelField<T?>>() {
    override val value: ViewModelField<T?> = ViewModelField(savedStateHandle, tag)
        get() {
            if (field.value == null) {
                field.value = savedStateHandle[tag]
            }

            if (field.value == null) {
                field.value = factory?.invoke()
            }
            return field
        }

    override operator fun getValue(
        thisRef: BaseViewModel,
        property: KProperty<*>
    ): ViewModelField<T?> {
        return value
    }
}
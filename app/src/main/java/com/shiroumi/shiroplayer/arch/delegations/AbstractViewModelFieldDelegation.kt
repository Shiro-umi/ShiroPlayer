package com.shiroumi.shiroplayer.arch.delegations

import com.shiroumi.shiroplayer.arch.BaseViewModel
import kotlin.reflect.KProperty

abstract class AbstractViewModelFieldDelegation<T> {
    abstract val value: T
    abstract operator fun getValue(thisRef: BaseViewModel, property: KProperty<*>): T
}
package com.shiroumi.scp.arch.delegations

import com.shiroumi.scp.arch.BaseViewModel
import kotlin.reflect.KProperty

abstract class AbstractViewModelFieldDelegation<T> {
    abstract val value: T
    abstract operator fun getValue(thisRef: BaseViewModel, property: KProperty<*>): T
}
package com.shiroumi.shiroplayer.arch.delegations

import com.shiroumi.shiroplayer.arch.viewmodel.BaseStatefulViewModel
import kotlin.reflect.KProperty

abstract class AbstractViewModelFieldDelegation<T> {
    abstract val value: T
    abstract operator fun getValue(thisRef: BaseStatefulViewModel, property: KProperty<*>): T
}
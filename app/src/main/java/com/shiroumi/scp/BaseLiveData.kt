package com.shiroumi.scp

import androidx.lifecycle.MutableLiveData
import com.shiroumi.scp.delegations.LiveDataDelegation

class BaseLiveData<T>(
    private val delegation: LiveDataDelegation<T>
) : MutableLiveData<T>() {
    override fun setValue(value: T?) {
        delegation.store()
        super.setValue(value)
    }
}
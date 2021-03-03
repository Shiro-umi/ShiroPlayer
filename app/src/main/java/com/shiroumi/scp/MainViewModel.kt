package com.shiroumi.scp

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.scp.network.Client
import com.shiroumi.scp.network.doEnqueue
import com.shiroumi.scp.network.doFailure
import com.shiroumi.scp.network.doSuccess


const val SAVED_INDEX_PAGE = "saved_state:index_page"

class MainViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val indexPage: MutableLiveData<String> by Live(savedStateHandle)

    fun getHTML() {

        Client.service.getHtml().doEnqueue { response, throwable ->
            response?.apply {
                doSuccess { indexPage.value = response.body().toString() }
                doFailure { Log.e("getHTML", throwable?.message.toString()) }
            }
            Log.e("getHTML", throwable?.message.toString())
        }
    }
}

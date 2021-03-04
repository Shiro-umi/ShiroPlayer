package com.shiroumi.scp

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.shiroumi.scp.arch.BaseViewModel
import com.shiroumi.scp.network.Client
import com.shiroumi.scp.network.PATH_INDEX
import com.shiroumi.scp.network.doEnqueue
import com.shiroumi.scp.network.doSuccess
import org.jsoup.Jsoup

const val TAG_MAIN_VIEW_MODEL = "log:main_view_model"
const val KEY_AUTO_INITIALIZED = "key:auto_initialized"

class MainViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(savedStateHandle) {
    val title: MutableLiveData<String> by viewModelStateDelegation.byLiveData("main_view_model_title")

    fun getDocument() {
        Client.service.getDocument(PATH_INDEX).doEnqueue { res, t ->
            res?.doSuccess {
                title.value = Jsoup.parse(it)
                    .getElementById("header")
                    .getElementsByTag("span")[0]
                    .text()
            }
            t?.let { Log.e(TAG_MAIN_VIEW_MODEL, t.message.toString()) }
        }
    }
}

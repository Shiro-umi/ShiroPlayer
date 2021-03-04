package com.shiroumi.scp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.shiroumi.scp.delegations.ViewModelStateDelegation
import com.shiroumi.scp.network.Client
import com.shiroumi.scp.network.PATH_INDEX
import com.shiroumi.scp.network.doEnqueue
import com.shiroumi.scp.network.doSuccess
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val TAG_MAIN_VIEW_MODEL = "log:main_view_model"
const val KEY_AUTO_INITIALIZED = "key:auto_initialized"

class MainViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(savedStateHandle) {
    val title: MutableLiveData<String> by viewModelStateDelegation.byLiveData("main_view_model_title") { "TITLE" }

    fun getDocument() {
        Client.service.getDocument(PATH_INDEX).doEnqueue { res, t ->
            res?.doSuccess {
                title.value = it.apply {
                    Jsoup.parse(this)
                        .getElementById("header")
                        .getElementsByTag("span")[0]
                        .text()
                }
            }
            t?.let { Log.e(TAG_MAIN_VIEW_MODEL, t.message.toString()) }
        }
    }
}

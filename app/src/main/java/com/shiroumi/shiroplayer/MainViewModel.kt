package com.shiroumi.shiroplayer

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.shiroumi.shiroplayer.arch.BaseViewModel
import com.shiroumi.shiroplayer.arch.delegations.ViewModelField
import com.shiroumi.shiroplayer.network.Client
import com.shiroumi.shiroplayer.network.PATH_INDEX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

const val TAG_MAIN_VIEW_MODEL = "log:main_view_model"
const val KEY_AUTO_INITIALIZED = "key:auto_initialized"

class MainViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle
) : BaseViewModel(savedStateHandle) {
    val title: MutableLiveData<String> by viewModelStateDelegation.byLiveData("main_view_model_title")
    val document: ViewModelField<Document?> by viewModelStateDelegation.byField("main_view_model_document",null)

    fun getDocument() {
        viewModelScope.launch {
            title.value = withContext(Dispatchers.IO) {
                val document = Client.service.getDocument(PATH_INDEX)
                Jsoup.parse(document)
                    .getElementById("header")
                    .getElementsByTag("span")[0]
                    .text()
            }
        }
    }

    override fun initialize() {
        getDocument()
    }
}

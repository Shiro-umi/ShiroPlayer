package com.shiroumi.scp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.shiroumi.scp.network.Client
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    fun getHTML() {
        Client.service.getHtml().enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Log.e("test", response.body().toString())
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.e("test", t.message.toString())
            }
        })
    }
}
package com.shiroumi.shiroplayer.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun <T> Call<T>.doEnqueue(block: ((Response<T>?, Throwable?) -> Unit)?) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            block?.invoke(response, null)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            block?.invoke(null, t)
        }
    })
}

fun <T> Response<T>.doSuccess(block: (T) -> Unit) {
    this.body()?.let(block)
}

fun <T> Response<T>.doFailure(block: () -> Unit) {
    if (this.body() == null) block.invoke()
}

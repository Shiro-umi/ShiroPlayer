package com.shiroumi.scp.network

import retrofit2.Call
import retrofit2.http.GET


interface Service {
    @GET("/")
    fun getHtml(): Call<String?>
}
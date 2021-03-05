package com.shiroumi.scp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface Service {
    @GET("{path}")
    suspend fun getDocument(@Path("path") path: String): String
}
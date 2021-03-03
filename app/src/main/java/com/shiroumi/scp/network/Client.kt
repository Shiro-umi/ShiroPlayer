package com.shiroumi.scp.network

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object Client {
    private val ua = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16"

    private val client: OkHttpClient
        get() = OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", ua)
                    .build()
                chain.proceed(request)
            }
            .build()

    val service: Service by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://scp-wiki-cn.wikidot.com/")
            .addConverterFactory(StringConverterFactory())
            .build()
            .create(Service::class.java)
    }
}
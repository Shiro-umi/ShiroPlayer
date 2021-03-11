package com.shiroumi.shiroplayer

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShiroApplication : Application() {
    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
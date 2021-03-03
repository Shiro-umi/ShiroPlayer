package com.shiroumi.scp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScpApplication : Application() {
    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
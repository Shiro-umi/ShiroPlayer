package com.shiroumi.shiroplayer.arch

import android.app.Application

lateinit var application: ShiroPlayerApplication
    private set

class ShiroPlayerApplication : Application() {
    override fun onCreate() {
        application = this
        super.onCreate()
    }
}
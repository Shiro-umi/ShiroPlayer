package com.shiroumi.shiroplayer.components

import android.content.Context

interface Playable<T> {
    fun play(context: Context): T
    fun stop()
}
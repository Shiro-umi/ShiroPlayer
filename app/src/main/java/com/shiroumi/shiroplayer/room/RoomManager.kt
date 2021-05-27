package com.shiroumi.shiroplayer.room

import android.content.Context
import androidx.room.Room

object RoomManager {
    private const val DB_NAME = "shiro-umi.db"
    lateinit var db: MusicDatabase

    fun init(context: Context) {
        if (::db.isInitialized) return
        db = Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            DB_NAME
        )
            .allowMainThreadQueries()
            .build()
    }
}
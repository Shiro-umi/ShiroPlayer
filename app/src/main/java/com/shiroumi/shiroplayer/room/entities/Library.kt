package com.shiroumi.shiroplayer.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class Library(
    @PrimaryKey
    val path: String
)
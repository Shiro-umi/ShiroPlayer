package com.shiroumi.shiroplayer.room.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class Music(
    @PrimaryKey(autoGenerate = true)
    var musicId: Long = -1,
    @ColumnInfo(name = "title")
    var musicTitle: String = "",
    @ColumnInfo(name = "artist")
    var artist: String = "",
    @ColumnInfo(name = "album")
    var album: String = "",
    @ColumnInfo(name = "duration")
    var duration: Float = 0f,
    @ColumnInfo(name = "uri")
    var uri: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(musicId)
        parcel.writeString(musicTitle)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeFloat(duration)
        parcel.writeString(uri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }
}
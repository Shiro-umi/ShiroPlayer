package com.shiroumi.shiroplayer.room.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class Music(
    @ColumnInfo(name = "title")
    var musicTitle: String = "",
    @ColumnInfo(name = "artist")
    var artist: String = "",
    @ColumnInfo(name = "album")
    var album: String = "",
    @ColumnInfo(name = "duration")
    var duration: Float = 0f,
    @PrimaryKey
    var uri: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        with(parcel) {
            writeString(musicTitle)
            writeString(artist)
            writeString(album)
            writeFloat(duration)
            writeString(uri)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    fun readFromParcel(parcel: Parcel) {
        with(parcel) {
            readLong()
            readString()
            readString()
            readString()
            readFloat()
            readString()
        }
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Music) return false
        if (musicTitle == other.musicTitle &&
                album == other.album &&
                artist == other.artist &&
                duration == other.duration &&
                uri == other.uri) {
            return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = musicTitle.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + album.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + uri.hashCode()
        return result
    }
}
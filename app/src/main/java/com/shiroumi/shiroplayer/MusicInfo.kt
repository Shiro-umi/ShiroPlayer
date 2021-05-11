package com.shiroumi.shiroplayer

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class MusicInfo(
    val music: Music?,
    val cover: Bitmap?,
    val index: Int?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Music::class.java.classLoader),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(music, flags)
        parcel.writeParcelable(cover, flags)
        parcel.writeValue(index)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicInfo> {
        override fun createFromParcel(parcel: Parcel): MusicInfo {
            return MusicInfo(parcel)
        }

        override fun newArray(size: Int): Array<MusicInfo?> {
            return arrayOfNulls(size)
        }
    }

}
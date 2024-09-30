package com.asiaegypt.adventu.ui.offerwall.model

import android.os.Parcel
import android.os.Parcelable

data class Game(
    val moduleCode: Int? = 0,
    val levelTitle: String = "",
    val visualFile: String = "",
    val auxImage: String = "",
    val activateText: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Game::class.java.classLoader) as? Int,
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(moduleCode)
        parcel.writeString(levelTitle)
        parcel.writeString(visualFile)
        parcel.writeString(auxImage)
        parcel.writeString(activateText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Game> {
        override fun createFromParcel(parcel: Parcel): Game {
            return Game(parcel)
        }

        override fun newArray(size: Int): Array<Game?> {
            return arrayOfNulls(size)
        }
    }
}

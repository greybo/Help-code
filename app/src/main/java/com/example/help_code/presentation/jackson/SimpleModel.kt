package com.example.help_code.presentation.jackson

import android.os.Parcel
import android.os.Parcelable

class SimpleModel() : Parcelable {
    var x: String? = null
    var y: String? = null

    constructor(x: String?, y: String?) : this() {
        this.x = x
        this.y = y
    }

    constructor(parcel: Parcel) : this(
        x = parcel.readString(),
        y = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(x)
        parcel.writeString(y)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SimpleModel> {
        override fun createFromParcel(parcel: Parcel): SimpleModel {
            return SimpleModel(parcel)
        }

        override fun newArray(size: Int): Array<SimpleModel?> {
            return arrayOfNulls(size)
        }
    }

}
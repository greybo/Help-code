package com.example.help_code.presentation.jackson

import android.os.Parcel
import android.os.Parcelable

class TestModel() : Parcelable {
    var inner: TestInnerModel? = null

    constructor(inner: TestInnerModel?) : this() {
        this.inner = inner
    }

    constructor(parcel: Parcel) : this(TestInnerModel(parcel)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TestModel> {
        override fun createFromParcel(parcel: Parcel): TestModel {
            return TestModel(parcel)
        }

        override fun newArray(size: Int): Array<TestModel?> {
            return arrayOfNulls(size)
        }
    }
}

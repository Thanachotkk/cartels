package com.internship.nilecon.cartels.Parkinglist.dataclass

import android.os.Parcel
import android.os.Parcelable

class child: Parcelable {
    var name: String? = null
    constructor(name: String) {
        this.name = name

    }

    protected constructor(`in`: Parcel) {
        name = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(name)
    }

    companion object {

        val CREATOR: Parcelable.Creator<child> = object : Parcelable.Creator<child> {
            override fun createFromParcel(`in`: Parcel): child {
                return child(`in`)
            }

            override fun newArray(size: Int): Array<child?> {
                return arrayOfNulls(size)
            }
        }
    }
}

package com.sharonovnik.homework_2.ui.profile

import android.content.Context
import android.os.Parcel
import android.os.Parcelable

data class UserInfoItem(
    val index: Int,
    val title: StringHolder,
    val subtitle: String,
    val iconResId: Int
) : Parcelable {
    var section: UserInfoSection? = null

    constructor(parcel: Parcel) : this(
        parcel.readInt(), parcel.readParcelable(StringHolder::class.java.classLoader)!!,
        parcel.readString()!!, parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(index)
        parcel.writeParcelable(title, flags)
        parcel.writeString(subtitle)
        parcel.writeInt(iconResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserInfoItem> {
        override fun createFromParcel(parcel: Parcel): UserInfoItem {
            return UserInfoItem(parcel)
        }

        override fun newArray(size: Int): Array<UserInfoItem?> {
            return arrayOfNulls(size)
        }
    }

}

internal fun UserInfoItem.attachToSection(section: UserInfoSection): UserInfoItem {
    this.section = section
    return this
}

data class StringHolder(val resId: Int? = null, val string: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int, parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(resId)
        parcel.writeString(string)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StringHolder> {
        override fun createFromParcel(parcel: Parcel): StringHolder {
            return StringHolder(parcel)
        }

        override fun newArray(size: Int): Array<StringHolder?> {
            return arrayOfNulls(size)
        }
    }

    fun getString(context: Context): String {
        if (resId == null) {
            return string ?: ""
        }
        return context.getString(resId)
    }
}
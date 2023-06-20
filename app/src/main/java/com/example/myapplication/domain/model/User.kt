@file:Suppress("DEPRECATION")

package com.example.myapplication.domain.model

import android.os.Parcel
import android.os.Parcelable
import android.text.format.DateUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.Date

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val profile: String,
    val gender: String,
    val address: String,
    val country: String,
    val registrationDate: Date?
) : Parcelable {

    constructor(source: Parcel): this(
        id = source.readString()!!,
        firstName = source.readString()!!,
        lastName = source.readString()!!,
        email = source.readString()!!,
        phone = source.readString()!!,
        profile = source.readString()!!,
        gender = source.readString()!!,
        address = source.readString()!!,
        country = source.readString()!!,
        registrationDate = source.readSerializable() as Date?,
    )


    fun getRelativeTime(): String? {
        return registrationDate?.let { DateUtils.getRelativeTimeSpanString(it.time) as String? }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.id)
        dest.writeString(this.firstName)
        dest.writeString(this.lastName)
        dest.writeString(this.email)
        dest.writeString(this.phone)
        dest.writeString(this.profile)
        dest.writeString(this.gender)
        dest.writeString(this.address)
        dest.writeString(this.country)
        dest.writeSerializable(this.registrationDate)
    }

    companion object {
        @JvmField final val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User{
                return User(source)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }
}

@BindingAdapter("profileImage")
fun loadImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions())
        .into(view)
}

@BindingAdapter("detailProfileImage")
fun loadDetailImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions())
        .into(view)
}



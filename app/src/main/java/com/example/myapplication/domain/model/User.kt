@file:Suppress("DEPRECATION")

package com.example.myapplication.domain.model

import android.os.Parcel
import android.os.Parcelable
import android.text.format.DateUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val profile: String,
    val gender: String,
    val street: String,
    val city: String,
    val state: String,
    val postcode: String,
    val country: String,
    val age: String,
    val dob: Date?,
    val registrationDate: Date?
) : Parcelable {

    constructor(source: Parcel) : this(
        id = source.readString()!!,
        firstName = source.readString()!!,
        lastName = source.readString()!!,
        email = source.readString()!!,
        phone = source.readString()!!,
        profile = source.readString()!!,
        gender = source.readString()!!,
        street = source.readString()!!,
        city = source.readString()!!,
        state = source.readString()!!,
        postcode = source.readString()!!,
        country = source.readString()!!,
        age = source.readString()!!,
        dob = source.readSerializable() as Date?,
        registrationDate = source.readSerializable() as Date?,
    )


    fun getRelativeTime(): String? {
        return registrationDate?.let { DateUtils.getRelativeTimeSpanString(it.time) as String? }
    }

    fun getDateOfBirth(): String? {
        return dob?.let {
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
        }
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
        dest.writeString(this.street)
        dest.writeString(this.city)
        dest.writeString(this.state)
        dest.writeString(this.postcode)
        dest.writeString(this.country)
        dest.writeString(this.age)
        dest.writeSerializable(this.dob)
        dest.writeSerializable(this.registrationDate)
    }

    companion object {
        @JvmField
        final val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User {
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
        .load(imageUrl).apply(RequestOptions().circleCrop())
        .into(view)

}

@BindingAdapter("detailProfileImage")
fun loadDetailImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}



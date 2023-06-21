package com.example.myapplication.data.source.remote.model

data class UserDTO(
    val gender: String, val name: UserName, val location: UserLocation, val email: String,
    val phone: String,
    val picture: UserPicture,
    val id: UserId,
    val login : UserLogin,
    val dob: UserDOB,
    val registered : UserRegistration,
) {
    data class UserName(val title: String, val first: String, val last: String);

    data class UserLogin(val uuid: String);

    data class UserLocation(
        val street: UserLocationStreet,
        val city: String,
        val state: String,
        val country: String,
        val postcode: String,
        val coordinates: UserLocationCoordinates,
    ) {
        data class UserLocationStreet(val number: Int, val name: String)
        data class UserLocationCoordinates(val latitude: Double, val longitude: Double)
    }

    data class UserDOB(val date: String, val age: Int)
    data class UserRegistration(val date: String)

    data class UserPicture(val large: String, val medium: String, val thumbnail: String)

    data class UserId(val name: String, val value: String)
}

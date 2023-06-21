package com.example.myapplication.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
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
    val registrationDate: String,
    val dob: String,
    val age: Int,
    var page: Int,
)
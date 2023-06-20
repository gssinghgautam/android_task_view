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
    val address: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val registrationDate: String,
    var page: Int,
)
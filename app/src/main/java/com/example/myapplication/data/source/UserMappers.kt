package com.example.myapplication.data.source

import com.example.myapplication.data.source.local.entity.UserEntity
import com.example.myapplication.data.source.remote.model.UserDTO
import com.example.myapplication.domain.model.User
import java.text.SimpleDateFormat
import java.util.Locale

fun UserDTO.toUserEntity(): UserEntity {
    return UserEntity(
        id = login.uuid,
        firstName = name.first,
        lastName = name.last,
        email = email,
        phone = phone,
        profile = picture.large,
        gender = gender,
        address = "${location.street.number}, ${location.street.name}",
        country = location.country,
        latitude = location.coordinates.latitude,
        longitude = location.coordinates.longitude,
        registrationDate = registered.date,
        page = 0
    )
}

fun List<UserDTO>.toUserEntities() = map(UserDTO::toUserEntity)

fun UserEntity.toUser(): User {
    return User(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        profile = profile,
        gender = gender,
        address = address,
        country= country,
        registrationDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(registrationDate),
    )
}

fun List<UserEntity>.toUsers() = map(UserEntity::toUser)

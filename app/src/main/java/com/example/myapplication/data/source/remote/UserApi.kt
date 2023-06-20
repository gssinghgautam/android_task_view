package com.example.myapplication.data.source.remote

import android.telecom.Call
import com.example.myapplication.data.source.remote.model.UserDTO
import com.example.myapplication.data.source.remote.model.UserResponseDTO
import com.example.myapplication.domain.model.User
import retrofit2.http.GET
import retrofit2.http.Query


interface UserApi {

    @GET("api")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("results") results: Int
    ): UserResponseDTO

    companion object {
        const val BASE_URL = "https://randomuser.me/"
    }
}
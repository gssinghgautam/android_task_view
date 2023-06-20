package com.example.myapplication.data.source.remote.model

data class UserResponseDTO(val results: List<UserDTO>, val info: ResponseInfoDTO) {
    data class ResponseInfoDTO(
        val seed: String,
        val results: Int,
        val page: Int,
        val version: String
    )
}
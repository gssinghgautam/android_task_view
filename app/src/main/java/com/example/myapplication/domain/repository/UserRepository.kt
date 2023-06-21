package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.myapplication.domain.model.User

interface UserRepository {

    fun getUsers(): LiveData<PagingData<User>>
}
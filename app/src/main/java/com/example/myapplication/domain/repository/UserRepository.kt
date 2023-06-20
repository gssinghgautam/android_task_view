package com.example.myapplication.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.myapplication.data.source.local.entity.UserEntity
import com.example.myapplication.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserRepository {

    fun getUsers(): LiveData<PagingData<User>>
}
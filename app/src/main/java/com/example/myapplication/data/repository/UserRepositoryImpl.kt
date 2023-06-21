package com.example.myapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.example.myapplication.data.source.local.entity.UserEntity
import com.example.myapplication.data.source.toUser
import com.example.myapplication.domain.model.User
import com.example.myapplication.domain.repository.UserRepository
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val pager: Pager<Int, UserEntity>
) : UserRepository {

    override fun getUsers(): LiveData<PagingData<User>> {
        return pager.liveData.switchMap {
            MutableLiveData(it.map { entity -> entity.toUser() })
        }
    }


}
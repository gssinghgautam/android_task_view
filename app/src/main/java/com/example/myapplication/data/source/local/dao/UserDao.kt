package com.example.myapplication.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.myapplication.data.source.local.entity.UserEntity

@Dao
interface UserDao{

    @Query("SELECT * FROM users")
    fun getAllUsers() : List<UserEntity>

    @Upsert
    suspend fun upsertAllUsers(users: List<UserEntity>)

    @Upsert
    suspend fun upsertUser(movie: UserEntity)

    @Query("SELECT * FROM users")
    fun pagingSource(): PagingSource<Int, UserEntity>

    @Query("DELETE FROM users")
    suspend fun clearAll()
}
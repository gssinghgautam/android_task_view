package com.example.myapplication.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.source.local.dao.RemoteKeysDao
import com.example.myapplication.data.source.local.dao.UserDao
import com.example.myapplication.data.source.local.entity.RemoteKeysEntity
import com.example.myapplication.data.source.local.entity.UserEntity

@Database(entities = [UserEntity::class, RemoteKeysEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}
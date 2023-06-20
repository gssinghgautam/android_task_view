package com.example.myapplication.di

import com.example.myapplication.data.source.local.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.room.Room
import com.example.myapplication.data.repository.UserRepositoryImpl
import com.example.myapplication.data.source.local.dao.RemoteKeysDao
import com.example.myapplication.data.source.local.dao.UserDao
import com.example.myapplication.data.source.local.entity.UserEntity
import com.example.myapplication.data.source.remote.UserApi
import com.example.myapplication.data.source.remote.UserRemoteMediator
import com.example.myapplication.domain.repository.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesUserDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            name = "user.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUsersDao(userDatabase: UserDatabase): UserDao = userDatabase.userDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(userDatabase: UserDatabase): RemoteKeysDao =
        userDatabase.remoteKeysDao()

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Provides
    fun provideMoviePager(
        userDatabase: UserDatabase,
        userApi: UserApi
    ): Pager<Int, UserEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10,
                initialLoadSize = 20,
            ),
            remoteMediator = UserRemoteMediator(userDatabase, userApi),
            pagingSourceFactory = {
                userDatabase.userDao().pagingSource()
            }
        )
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(UserApi.BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesUsersApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesUserRepository(pager: Pager<Int, UserEntity>): UserRepository {
        return UserRepositoryImpl(pager)
    }
}
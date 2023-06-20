@file:OptIn(ExperimentalPagingApi::class)

package com.example.myapplication.data.source.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.example.myapplication.data.source.local.database.UserDatabase
import com.example.myapplication.data.source.local.entity.UserEntity
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.room.withTransaction
import com.example.myapplication.data.source.local.entity.RemoteKeysEntity
import com.example.myapplication.data.source.toUserEntities
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val localDataSource: UserDatabase,
    private val remoteDataSource: UserApi
) : RemoteMediator<Int, UserEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val creationTime = localDataSource.remoteKeysDao()
            .getCreationTime()

        return if (creationTime !=null && System.currentTimeMillis() - creationTime < cacheTimeout
        ) {
            // Cached data is up-to-date, so there is no need to re-fetch
            // from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UserEntity>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { user ->
            localDataSource.remoteKeysDao().getRemoteKeyByUserID(user.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { user ->
            localDataSource.remoteKeysDao().getRemoteKeyByUserID(user.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UserEntity>): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                localDataSource.remoteKeysDao().getRemoteKeyByUserID(id)
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                //New Query so clear the DB
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val apiResponse = remoteDataSource.getUsers(page = page, results = 20)

            delay(1000L) //TODO For testing only!

            val users = apiResponse.results
            val endOfPaginationReached = users.isEmpty()


            localDataSource.withTransaction {
                if (loadType == LoadType.REFRESH && users.isNotEmpty()) { //New query so we can delete everything.
                    localDataSource.remoteKeysDao().clearRemoteKeys()
                    localDataSource.userDao().clearAll()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = users.map {
                    RemoteKeysEntity(
                        userId = it.login.uuid,
                        prevKey = prevKey,
                        currentPage = page,
                        nextKey = nextKey
                    )
                }

                localDataSource.remoteKeysDao().insertAll(remoteKeys)
                localDataSource.userDao().upsertAllUsers(
                    users.toUserEntities().onEachIndexed { _, user -> user.page = page })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        }
    }
}
package ru.netology.nmedia.repository

import androidx.paging.*
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostRemoteKey
import ru.netology.nmedia.error.ApiError

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val postDao: PostDao,
    private val apiService: ApiService,
    private val postRemoteKeyDao: PostRemoteKeyDao
) : RemoteMediator<Long, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Long, PostEntity>
    ): MediatorResult {
        return try {
            val response = when (loadType) {
                LoadType.REFRESH -> {

                    val response = apiService.getLatest(state.config.pageSize)
                    if (!response.isSuccessful) {
                        throw ApiError(response.code(), response.message())
                    }
                    response.body() ?: emptyList()
                }
                LoadType.APPEND -> {

                    val remoteKey = postRemoteKeyDao.getKeyByPostId(state.anchorPosition ?: return MediatorResult.Success(endOfPaginationReached = true))
                    val response = apiService.getAfter(remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true), state.config.pageSize)
                    if (!response.isSuccessful) {
                        throw ApiError(response.code(), response.message())
                    }
                    response.body() ?: emptyList()
                }
                LoadType.PREPEND -> {

                    val remoteKey = postRemoteKeyDao.getKeyByPostId(state.anchorPosition ?: return MediatorResult.Success(endOfPaginationReached = true))
                    val response = apiService.getBefore(remoteKey?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true), state.config.pageSize)
                    if (!response.isSuccessful) {
                        throw ApiError(response.code(), response.message())
                    }
                    response.body() ?: emptyList()
                }
            }

            postDao.insert(response.map { PostEntity.fromDto(it) })

            val nextKey = if (response.isEmpty()) null else response.last().id
            val prevKey = if (loadType == LoadType.PREPEND) response.first().id else null
            postRemoteKeyDao.insert(PostRemoteKey(postId = state.anchorPosition ?: 0, prevKey = prevKey, nextKey = nextKey))

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
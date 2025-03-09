package ru.netology.nmedia.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nmedia.api.ApiService
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.error.ApiError

class PostPagingSource(
    private val service: ApiService,
) : PagingSource<Long, Post>() {
    override fun getRefreshKey(state: PagingState<Long, Post>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        return try {
            val response = when (params) {
                is LoadParams.Refresh -> service.getLatest(params.loadSize)
                is LoadParams.Append -> service.getAfter(params.key, params.loadSize)
                is LoadParams.Prepend -> service.getBefore(params.key, params.loadSize)
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())

            val nextKey = if (body.isEmpty()) null else body.last().id
            LoadResult.Page(
                data = body,
                prevKey = params.key,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
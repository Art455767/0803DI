package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PostRemoteKey(
    @PrimaryKey val postId: Int,
    val prevKey: Long?,
    val nextKey: Long?
)
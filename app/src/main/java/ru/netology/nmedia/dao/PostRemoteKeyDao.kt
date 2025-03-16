package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.entity.PostRemoteKey

@Dao
interface PostRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(key: PostRemoteKey)

    @Query("SELECT * FROM PostRemoteKey WHERE postId = :postId")
    suspend fun getKeyByPostId(postId: Int): PostRemoteKey?

    @Query("DELETE FROM PostRemoteKey")
    suspend fun clear()
}
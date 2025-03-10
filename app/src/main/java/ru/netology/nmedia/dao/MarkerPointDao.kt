package ru.netology.nmedia.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.netology.nmedia.entity.MarkerPoint

@Dao
interface MarkerPointDao {

    @Insert
    suspend fun insert(markerPoint: MarkerPoint)

    @Update
    suspend fun update(markerPoint: MarkerPoint)

    @Delete
    suspend fun delete(markerPoint: MarkerPoint)

    @Query("SELECT * FROM marker_points")
    suspend fun getAll(): List<MarkerPoint>
}
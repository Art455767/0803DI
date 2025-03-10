package ru.netology.nmedia.repository

import ru.netology.nmedia.dao.MarkerPointDao
import ru.netology.nmedia.entity.MarkerPoint

class MarkerRepository(private val markerPointDao: MarkerPointDao) {
    suspend fun addMarker(markerPoint: MarkerPoint) {
        markerPointDao.insert(markerPoint)
    }

    suspend fun updateMarker(markerPoint: MarkerPoint) {
        markerPointDao.update(markerPoint)
    }

    suspend fun deleteMarker(markerPoint: MarkerPoint) {
        markerPointDao.delete(markerPoint)
    }

    suspend fun getAllMarkers(): List<MarkerPoint> {
        return markerPointDao.getAll()
    }
}
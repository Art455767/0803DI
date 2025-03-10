package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.entity.MarkerPoint
import ru.netology.nmedia.repository.MarkerRepository

class MarkerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MarkerRepository
    private val _allMarkers = MutableLiveData<List<MarkerPoint>>()
    var allMarkers: LiveData<List<MarkerPoint>> get() = _allMarkers

    init {
        val markerPointDao = MarkerDatabase.getDatabase(application).markerPointDao()
        repository = MarkerRepository(markerPointDao)
        allMarkers = MutableLiveData()
        loadMarkers()
    }

    private fun loadMarkers() {
        viewModelScope.launch {
            _allMarkers.value = repository.getAllMarkers()
        }
    }

    fun addMarker(markerPoint: MarkerPoint) {
        viewModelScope.launch {
            repository.addMarker(markerPoint)
            loadMarkers()
        }
    }

    fun updateMarker(markerPoint: MarkerPoint) {
        viewModelScope.launch {
            repository.updateMarker(markerPoint)
            loadMarkers()
        }
    }

    fun deleteMarker(markerPoint: MarkerPoint) {
        viewModelScope.launch {
            repository.deleteMarker(markerPoint)
            loadMarkers()
        }
    }
}
package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import ru.netology.nmedia.R
import ru.netology.nmedia.entity.MarkerPoint
import ru.netology.nmedia.viewmodel.MarkerViewModel

class MapFragment : Fragment() {
    private lateinit var map: GoogleMap
    private lateinit var viewModel: MarkerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MarkerViewModel::class.java]

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            setupMap()
        }
    }

    private fun setupMap() {
        map.setOnMapClickListener { latLng ->
            val markerPoint = MarkerPoint(latitude = latLng.latitude, longitude = latLng.longitude, description = "New Marker")
            viewModel.addMarker(markerPoint)
            map.addMarker(MarkerOptions().position(latLng).title(markerPoint.description))
        }

        viewModel.allMarkers.observe(viewLifecycleOwner) { markers ->
            markers.forEach { marker ->
                val latLng = LatLng(marker.latitude, marker.longitude)
                map.addMarker(MarkerOptions().position(latLng).title(marker.description))
            }
        }
    }
}
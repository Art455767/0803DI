package ru.netology.nmedia.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.MarkersAdapter
import ru.netology.nmedia.adapter.OnMarkerInteractionListener
import ru.netology.nmedia.entity.MarkerPoint
import ru.netology.nmedia.viewmodel.MarkerViewModel

class MapFragment : Fragment() {
    private lateinit var map: GoogleMap
    private lateinit var viewModel: MarkerViewModel
    private lateinit var markersAdapter: MarkersAdapter

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

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

        markersAdapter = MarkersAdapter(object : OnMarkerInteractionListener {
            override fun onEdit(marker: MarkerPoint) {
            }

            override fun onRemove(marker: MarkerPoint) {
            }
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = markersAdapter

        viewModel.allMarkers.observe(viewLifecycleOwner) { markers ->
            markersAdapter.submitData(viewLifecycleOwner.lifecycle, markers) 
        }
    }

    private fun setupMap() {
        checkLocationPermission()

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

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            map.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package com.example.cityaware


import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cityaware.MapsFragmentModel
import com.example.cityaware.R
import com.example.cityaware.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import java.io.IOException

class MapsFragment : Fragment(), OnMapReadyCallback {
    private var mapsFragmentModel: MapsFragmentModel? = null
    private var mMapView: MapView? = null
    private var searchView: SearchView? = null
    private var currentLocBtn: MaterialButton? = null
    private var map: GoogleMap? = null
    var geocoder: Geocoder? = null
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private var lastLatLng: LatLng? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var savedInstanceState: Bundle? = null
    var binding: FragmentMapsBinding? = null
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext())
        init()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_CODE
            )
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient!!.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLatLng =
                    LatLng(location.latitude, location.longitude)
                map!!.moveCamera(CameraUpdateFactory.newLatLng(lastLatLng!!))
                map!!.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM), 2000, null)
            }
        }
    }
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }
    @Deprecated("This method is deprecated", ReplaceWith("newMethod()"))
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = when {
            requestCode != PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                false
            }
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED -> true
            else -> false
        }
        updateLocationUI()
    }
    private fun updateLocationUI() {
        map?.let {
            try {
                if (locationPermissionGranted) {
                    it.isMyLocationEnabled = true
                    it.uiSettings.isMyLocationButtonEnabled = true
                } else {
                    it.isMyLocationEnabled = false
                    it.uiSettings.isMyLocationButtonEnabled = false
                    lastKnownLocation = null
                    getLocationPermission()
                }
            } catch (e: SecurityException) {
                // Handle the SecurityException here
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//     View   nView= inflater.inflate(R.layout.fragment_maps, container, false);
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        val nView = binding!!.root
        mMapView = nView.findViewById(R.id.map)
        searchView = nView.findViewById(R.id.idSearchView)
        this.savedInstanceState = savedInstanceState
        currentLocBtn = nView.findViewById(R.id.current_loc_btn)
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            map!!.moveCamera(this.savedInstanceState!!.getParcelable(KEY_CAMERA_POSITION)!!)
        }
        initGoogleMap(this.savedInstanceState)
        return nView
    }

    private fun init() {
        map!!.setOnMapClickListener { map_click: LatLng ->
            lastLatLng =
                LatLng(map_click.latitude, map_click.longitude)
            var locationName = lastLatLng.toString()
            try {
                val address =
                    geocoder!!.getFromLocation(map_click.latitude, map_click.longitude, 1)
                if (address!!.size >= 1) {
                    locationName = address!![0].getAddressLine(0)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            changeMarker(locationName)
        }
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val location = searchView!!.query.toString()
                val addressList: List<Address>?
                val geocoder = Geocoder(context!!)
                try {
                    addressList = geocoder.getFromLocationName(location, 1)
                    if (addressList!!.size >= 1) {
                        val address = addressList!![0]
                        lastLatLng = LatLng(address.latitude, address.longitude)
                        changeMarker(address.getAddressLine(0))
                    } else {
                        Toast.makeText(context, "Location Not Found", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        currentLocBtn!!.setOnClickListener { btn_click: View? ->
            getCurrentLocation()
            if (lastLatLng != null) {
                var locationName = lastLatLng.toString()
                try {
                    val address =
                        geocoder!!.getFromLocation(
                            lastLatLng!!.latitude, lastLatLng!!.longitude, 1
                        )
                    if (address!!.size >= 1) {
                        locationName = address!![0].getAddressLine(0)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                changeMarker(locationName)
            }
        }
    }
    private fun changeMarker(title: String) {
        map!!.clear()
        map!!.addMarker(MarkerOptions().position(lastLatLng!!).title(title))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, DEFAULT_ZOOM))
        val marker = MarkerOptions()
            .position(lastLatLng!!)
            .title(title)
        map!!.clear()
        map!!.addMarker(marker)
        if (savedInstanceState == null) {
            savedInstanceState = Bundle()
        }
        savedInstanceState!!.putParcelable("locationTemp", lastLatLng)
        savedInstanceState!!.putString("locationNameTemp", title)
        mapsFragmentModel!!.setSavedInstanceStateData(savedInstanceState)
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView!!.onCreate(mapViewBundle)
        mMapView!!.getMapAsync(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(requireActivity())
        mapsFragmentModel = viewModelProvider[MapsFragmentModel::class.java]
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map!!.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastLatLng)
        }
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView!!.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onStart() {
        super.onStart()
        mMapView!!.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onStop() {
        super.onStop()
        mMapView!!.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION_CODE = 1
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234
        const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        private const val DEFAULT_ZOOM = 15f
    }
}
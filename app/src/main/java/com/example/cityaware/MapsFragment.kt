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
import com.example.cityaware.MyApplication.Companion.myContext
import com.example.cityaware.databinding.FragmentMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.button.MaterialButton
import java.io.IOException

class MapsFragment constructor() : Fragment(), OnMapReadyCallback {
    //Data.M
    private var mapsFragmentModel: MapsFragmentModel? = null
    private var mMapView: MapView? = null
    private var searchView: SearchView? = null
    private var currentLocBtn: MaterialButton? = null
    private var map: GoogleMap? = null
    var geocoder: Geocoder? = null
    private var locationPermissionGranted: Boolean = false
    private var lastKnownLocation: Location? = null
    private var lastLatLng: LatLng? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var savedInstanceState: Bundle? = null
    var binding: FragmentMapsBinding? = null
    public override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext())
        init()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            currentLocation
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_CODE
            )
            currentLocation
        }
    }

    private val currentLocation: Unit
        private get() {
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
            fusedLocationProviderClient!!.getLastLocation()
                .addOnSuccessListener(object : OnSuccessListener<Location?> {
                    public override fun onSuccess(location: Location?) {
                        if (location != null) {
                            lastLatLng = LatLng(location.getLatitude(), location.getLongitude())
                            map!!.moveCamera(CameraUpdateFactory.newLatLng(lastLatLng!!))
                            map!!.animateCamera(
                                CameraUpdateFactory.zoomTo(DEFAULT_ZOOM),
                                2000,
                                null
                            )
                        }
                    }
                })
        }
    private val locationPermission: Unit
        private get() {
            if ((ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                        == PackageManager.PERMISSION_GRANTED)
            ) {
                locationPermissionGranted = true
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }

    public override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        if ((requestCode
                    == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        ) { // If request is cancelled, the result arrays are empty.
            if ((grantResults.size > 0
                        && grantResults.get(0) == PackageManager.PERMISSION_GRANTED)
            ) {
                locationPermissionGranted = true
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map!!.setMyLocationEnabled(true)
                map!!.getUiSettings().setMyLocationButtonEnabled(true)
            } else {
                map!!.setMyLocationEnabled(false)
                map!!.getUiSettings().setMyLocationButtonEnabled(false)
                lastKnownLocation = null
                locationPermission
            }
        } catch (e: SecurityException) {
        }
    }

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//     View   nView= inflater.inflate(R.layout.fragment_maps, container, false);
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        val nView: View = binding!!.getRoot()
        mMapView = nView.findViewById(R.id.map)
        searchView = nView.findViewById(R.id.idSearchView)
        this.savedInstanceState = savedInstanceState
        currentLocBtn = nView.findViewById(R.id.current_loc_btn)
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            map!!.moveCamera((this.savedInstanceState!!.getParcelable(KEY_CAMERA_POSITION))!!)
        }
        initGoogleMap(this.savedInstanceState)
        /*binding.idSearchView.setOnClickListener(view -> {
            NavDirections action = MapsFragmentDirections.actionMapsFragmentToAddNewPostFragment(lastLatLng);
            Navigation.findNavController(view).navigate(action);

        });*/return nView
    }

    private fun init() {
        map!!.setOnMapClickListener(OnMapClickListener({ map_click: LatLng ->
            lastLatLng = LatLng(map_click.latitude, map_click.longitude)
            var locationName: String = lastLatLng.toString()
            try {
                val address: List<Address> =
                    geocoder!!.getFromLocation(map_click.latitude, map_click.longitude, 1)!!
                if (address.size >= 1) {
                    locationName = address.get(0).getAddressLine(0)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            changeMarker(locationName)
        }))
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            public override fun onQueryTextSubmit(query: String): Boolean {
                val location: String = searchView!!.getQuery().toString()
                val addressList: List<Address>
                val geocoder: Geocoder = Geocoder(requireContext())
                try {
                    addressList = geocoder.getFromLocationName(location, 1)!!
                    if (addressList.size >= 1) {
                        val address: Address = addressList.get(0)
                        lastLatLng = LatLng(address.getLatitude(), address.getLongitude())
                        changeMarker(address.getAddressLine(0))
                    } else {
                        Toast.makeText(getContext(), "Location Not Found", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                return false
            }

            public override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        currentLocBtn!!.setOnClickListener(View.OnClickListener({ btn_click: View? ->
            currentLocation
            if (lastLatLng != null) {
                var locationName: String = lastLatLng.toString()
                try {
                    val address: List<Address> = geocoder!!.getFromLocation(
                        lastLatLng!!.latitude, lastLatLng!!.longitude, 1
                    )!!
                    if (address.size >= 1) {
                        locationName = address.get(0).getAddressLine(0)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                changeMarker(locationName)
            }
        }))
    }

    private fun changeMarker(title: String) {
        map!!.clear()
        map!!.addMarker(MarkerOptions().position((lastLatLng)!!).title(title))
        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom((lastLatLng)!!, DEFAULT_ZOOM))
        val marker: MarkerOptions = MarkerOptions()
            .position((lastLatLng)!!)
            .title(title)
        map!!.clear()
        map!!.addMarker(marker)
        if (savedInstanceState == null) {
            savedInstanceState = Bundle()
        }
        savedInstanceState!!.putParcelable("locationTemp", lastLatLng)
        savedInstanceState!!.putString("locationNameTemp", title)
        mapsFragmentModel!!.savedInstanceStateData = savedInstanceState
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView!!.onCreate(mapViewBundle)
        mMapView!!.getMapAsync(this)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider: ViewModelProvider = ViewModelProvider(requireActivity())
        mapsFragmentModel = viewModelProvider.get(MapsFragmentModel::class.java)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle: Bundle? = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map!!.getCameraPosition())
            outState.putParcelable(KEY_LOCATION, lastLatLng)
        }
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView!!.onSaveInstanceState(mapViewBundle)
    }

    public override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    public override fun onStart() {
        super.onStart()
        mMapView!!.onStart()
        (getActivity() as AppCompatActivity?)!!.getSupportActionBar()!!.show()
    }

    public override fun onStop() {
        super.onStop()
        mMapView!!.onStop()
        (getActivity() as AppCompatActivity?)!!.getSupportActionBar()!!.hide()
    }

    companion object {
        private val REQUEST_LOCATION_PERMISSION_CODE: Int = 1
        private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 1234
        val MAPVIEW_BUNDLE_KEY: String = "MapViewBundleKey"
        private val KEY_CAMERA_POSITION: String = "camera_position"
        private val KEY_LOCATION: String = "location"
        private val DEFAULT_ZOOM: Float = 15f
    }
}
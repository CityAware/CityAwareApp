import android.Manifest
import com.example.cityaware.R
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class MapsFragment : Fragment(), OnMapReadyCallback {
    private var mMapView: MapView? = null
    var searchView: SearchView? = null
    var map: GoogleMap? = null
    var geocoder: Geocoder? = null
    private var locationPermissionGranted = false
    private var lastKnownLocation: Location? = null
    private val fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if (locationPermissionGranted) {
            deviceLocation
            updateLocationUI()
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = false
            init()
        }
    }

    private val deviceLocation: Unit
        private get() {
            try {
                if (locationPermissionGranted) {
                    val locationResult = fusedLocationProviderClient!!.lastLocation
                    locationResult.addOnCompleteListener(
                        this,
                        object : OnCompleteListener<Location?> {
                            override fun onComplete(task: Task<Location?>) {
                                if (task.isSuccessful) {
                                    // Set the map's camera position to the current location of the device.
                                    lastKnownLocation = task.result
                                    if (lastKnownLocation != null) {
                                        map!!.moveCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(
                                                    lastKnownLocation!!.latitude,
                                                    lastKnownLocation!!.longitude
                                                ), DEFAULT_ZOOM
                                            )
                                        )
                                    }
                                } else {
                                    val locationManager =
                                        this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                    val criteria = Criteria()
                                    val bestProvider =
                                        locationManager.getBestProvider(criteria, true)
                                    map!!.moveCamera(
                                        CameraUpdateFactory
                                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM)
                                    )
                                    map!!.uiSettings.isMyLocationButtonEnabled = false
                                }
                            }
                        })
                }
            } catch (e: SecurityException) {
            }
        }
    private val locationPermission: Unit
        private get() {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        if (requestCode
            == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
        ) { // If request is cancelled, the result arrays are empty.
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
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
                map!!.isMyLocationEnabled = true
                map!!.uiSettings.isMyLocationButtonEnabled = true
            } else {
                map!!.isMyLocationEnabled = false
                map!!.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                locationPermission
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val nView: View = inflater.inflate(R.layout.fragment_maps, container, false)
        mMapView = nView.findViewById<MapView>(R.id.map)
        searchView = nView.findViewById<SearchView>(R.id.idSearchView)
        geocoder = Geocoder(requireContext())
        initGoogleMap(savedInstanceState)
        return nView
    }

    private fun initGoogleMap(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView!!.onCreate(mapViewBundle)
        mMapView!!.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
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
    }

    override fun onStop() {
        super.onStop()
        mMapView!!.onStop()
    } //    @Override

    //    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    //        super.onViewCreated(view, savedInstanceState);
    //
    //
    //        }
    //    }
    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1234
        const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
        private const val DEFAULT_ZOOM = 15f
    }
}
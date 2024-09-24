package com.example.mobilneprojekat.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mobilneprojekat.R
import com.example.mobilneprojekat.UserViewModel
import com.example.mobilneprojekat.data.PasswordEventDB
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint
import java.util.Date
import kotlin.math.atan2
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var  lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var  locationCallback: LocationCallback
    private var viewModel: UserViewModel = UserViewModel()
    private lateinit var passwordEvents: ArrayList<PasswordEventDB>
    private var authorFilter: String? = null
    private var wifiTypesFilter: ArrayList<String>? = null
    private var passwordFilter: String? = null
    private var date : Date? = null
    private var radius: Double? = null
    private var passwordEventsUsable: ArrayList<PasswordEventDB> = ArrayList<PasswordEventDB>()


    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getPasswordEvents()

        authorFilter = arguments?.getString("author")
        wifiTypesFilter = arguments?.getStringArrayList("PasswordTypes")
        passwordFilter = arguments?.getString("outdoor")
        radius = arguments?.getDouble("radius")
        date = arguments?.getSerializable("date") as? Date
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.lastLocation?.let { location ->
                    // Handle the new location here
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)

                    mMap.clear()
                    if(passwordEventsUsable.isNotEmpty()){
                        val myGeoPoint = GeoPoint(lastLocation.latitude, lastLocation.longitude)

                        for(passwordEvent in passwordEventsUsable){
                            if(radius!=null){
                                if(radius!=0.0){
                                    if(!isPointWithinRadius(myGeoPoint, passwordEvent.location, radius!!))
                                        continue
                                }
                            }

//                            Log.d("Markeri", passwordEvent.id)
                            val marker = mMap.addMarker(MarkerOptions()
                            .position(LatLng(passwordEvent.location.latitude, passwordEvent.location.longitude))
                            .title(passwordEvent.ssid))

                            marker!!.tag = passwordEvent.id
                        }
                    }

                    updateCamera(currentLatLng)
                }
            }
        }

        val addObject = view.findViewById<Button>(R.id.addObjectMF)

        addObject.setOnClickListener{
            val fragment = CreatePasswordEventFragment()

            val bundle = Bundle()
            bundle.putDouble("lat", lastLocation.latitude)
            bundle.putDouble("lng", lastLocation.longitude)
            fragment.arguments = bundle

            val activity = requireActivity() as MainActivity
            val fragmentManager = activity.getFragmentManagerMA()
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, fragment)
            transaction.addToBackStack("create")
            transaction.commit()
        }

        val addFilter = view.findViewById<Button>(R.id.addFilterMF)

        addFilter.setOnClickListener{
            val fragment = FilterFragment()

            val bundle = Bundle()
            if(authorFilter==null)
                authorFilter=""
            bundle.putString("author", authorFilter)
            if(wifiTypesFilter==null)
                wifiTypesFilter = ArrayList<String>()
            bundle.putStringArrayList("PasswordTypes", wifiTypesFilter)
            if(passwordFilter==null)
                passwordFilter = ""
            bundle.putString("bandwidth", passwordFilter)
            if(date!=null)
                bundle.putSerializable("date", date)
            if(radius==null)
                radius = 0.0
            bundle.putDouble("radius", radius!!)

            fragment.arguments = bundle

            val activity = requireActivity() as MainActivity
            val fragmentManager = activity.getFragmentManagerMA()
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, fragment)
            transaction.addToBackStack("filter")
            transaction.commit()
        }

        return  view
    }

    private fun updateCamera(currentLatLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
    }

    fun calculateDistance(point1: GeoPoint, point2: GeoPoint): Double {
        val radiusOfEarth = 6371 // Earth's radius in kilometers

        val lat1 = Math.toRadians(point1.latitude)
        val lon1 = Math.toRadians(point1.longitude)
        val lat2 = Math.toRadians(point2.latitude)
        val lon2 = Math.toRadians(point2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return radiusOfEarth * c
    }

    fun isPointWithinRadius(myLoc: GeoPoint, markerLoc: GeoPoint, radiusKm: Double): Boolean {
        val distance = calculateDistance(myLoc, markerLoc)
        return distance <= radiusKm
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (isLocationPermissionGranted())
            mMap.isMyLocationEnabled = true

        startLocationUpdates()

        viewModel.passwordEvents.observe(viewLifecycleOwner, Observer { passwordEV ->
            if(passwordEV!= null){
                passwordEvents = passwordEV
                //val myGeoPoint = GeoPoint(lastLocation.latitude, lastLocation.longitude)

                for(passwordEvent in passwordEvents){

                    if(authorFilter!=null){
                        if(authorFilter!="" && passwordEvent.author != authorFilter)
                            continue
                    }
                    if(wifiTypesFilter!=null){
                        if(wifiTypesFilter!!.isNotEmpty() && passwordEvent.wifiType !in wifiTypesFilter!!)
                            continue
                    }
                    if(passwordFilter!=null){
                        if(passwordFilter!="") {
                            if (passwordFilter == "Out" && !passwordEvent.bandwidth)
                                continue
                            else if (passwordFilter == "In" && passwordEvent.bandwidth)
                                continue
                        }
                    }
                    if(date!=null){
                        if(date!!>passwordEvent.dateAdded.toDate())
                            continue
                    }
//                    if(radius!=null){
//                        if(radius!=0.0){
//                            if(!isPointWithinRadius(myGeoPoint, passwordEvent.location, radius!!))
//                                continue
//                        }
//                    }

//                    mMap.addMarker(MarkerOptions()
//                        .position(LatLng(passwordEvent.location.latitude, passwordEvent.location.longitude))
//                        .title("Marker")
//                      )
                    passwordEventsUsable.add(passwordEvent)
                }
            }
        })

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (isLocationPermissionGranted()) {
            val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
                interval = 10000 // Update location every 10 seconds (adjust as needed)
                fastestInterval = 5000 // Fastest update interval
                priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
                smallestDisplacement = 20.toFloat()
            }


            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        if (isLocationPermissionGranted())
            mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if(location != null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                //placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        // Retrieve the data from the marker.
        //val clickCount = marker.tag as? Int


        val myGeoPoint = GeoPoint(lastLocation.latitude, lastLocation.longitude)
        val markerGeoPoint = GeoPoint(marker.position.latitude, marker.position.longitude)

        if(isPointWithinRadius(myGeoPoint, markerGeoPoint, 0.05)) {

            Log.d("MarkerFUN", marker.tag as String)

            val fragment = PasswordEventFragment()

            val bundle = Bundle()
            bundle.putString("passwordEventId", marker.tag as String)
            fragment.arguments = bundle

            val activity = requireActivity() as MainActivity
            val fragmentManager = activity.getFragmentManagerMA()
            val transaction = fragmentManager.beginTransaction()

            transaction.replace(R.id.frame_layout, fragment)
            transaction.addToBackStack("sef")
            transaction.commit()
        }

        return false
    }


    private fun isLocationPermissionGranted() =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


}
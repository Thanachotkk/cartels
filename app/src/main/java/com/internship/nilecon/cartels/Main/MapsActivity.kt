package com.internship.nilecon.cartels.Main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.transition.TransitionManager
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.internship.nilecon.cartels.R
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.util.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener
        , GoogleMap.OnMarkerClickListener{


    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 777
    private lateinit var mMap: GoogleMap
    private var mLocationPermissionsGranted: Boolean? = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mGoogleApiClient: GoogleApiClient? = null
    private val DEFAULT_ZOOM = 16f
    private val places = mapOf(
            "PERTH" to LatLng(13.7748604, 100.5745226),
            "SYDNEY" to LatLng(13.7741698, 100.5754419),
            "rat2" to LatLng(13.7803506, 100.5748004),
            "BRISBANE" to LatLng(13.7705131, 100.5722809)
    )




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        setupButtonBack()
        setupLocationPermission()

    }


    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        if (mLocationPermissionsGranted!!) {

            getDeviceLocation()

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false

            init()

        }
        with(mMap) {

            this!!.setOnMarkerClickListener(this@MapsActivity)
        }
        addMarkersToMapCar()
    }

    private fun init() {

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()

        input_search.setOnEditorActionListener { textview, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.action == KeyEvent.ACTION_DOWN
                    || event.action == KeyEvent.KEYCODE_ENTER) {

                //execute our method for searching
                geoLocate()
            }

            return@setOnEditorActionListener false

        }

        ic_gps.setOnClickListener {

            print("onClick: clicked gps icon")
            getDeviceLocation()
        }

        hidesoftKeyboard()

    }

    private fun geoLocate() {

        print(" Geolocate : geolocating")

        val searchString = input_search.text.toString()
        val geocoder = Geocoder(this)
        var list: List<Address> = ArrayList()

        try {
            list = geocoder.getFromLocationName(searchString, 1)

        } catch (e: IOException) {

            print("geoLocate: IOException: " + e.message)

        }
        if (list.isNotEmpty()) {
            val address = list.get(0)

            print("geolocate found a location" + address.toString())

            moveCamera(LatLng(address.latitude, address.longitude), DEFAULT_ZOOM,
                    address.getAddressLine(0))

        }


    }

    private fun getDeviceLocation() {
        print("getDeviceLocation: getting the devices current location")

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        try {
            if (mLocationPermissionsGranted!!) {

                val location = mFusedLocationProviderClient!!.lastLocation
                location.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        print("onComplete: found location!")
                        val currentLocation = task.result as Location

                        moveCamera(LatLng(currentLocation.latitude, currentLocation.longitude),
                                DEFAULT_ZOOM, "My Location")

                    } else {
                        print("onComplete: current location is null")
                        Toast.makeText(this@MapsActivity, "unable to get current location", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } catch (e: SecurityException) {
            print("getDeviceLocation: SecurityException: " + e.message)
        }

    }

    private fun moveCamera(latLng: LatLng, zoom: Float, title: String) {
        print( "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))

        if (title != "My Location") {
            val options = MarkerOptions()
                    .position(latLng)
                    .title(title)
            mMap.addMarker(options)
        }
    }


    private fun initMap() {

        print("initMap : intialzing map")
        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment

        mapFragment.getMapAsync(this@MapsActivity)
    }

    private fun hidesoftKeyboard() {

        this.window.setSoftInputMode(WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE)
    }

    private fun setupLocationPermission() {
        print("setupLocationPermission: getting location permissions")

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.applicationContext,
                            COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true
                initMap()

            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE)
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        print("onRequestPermission : call")
        mLocationPermissionsGranted = false

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    for (i in grantResults.indices) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false
                            print("onRequestPermissionsResult: permission failed")
                            return
                        }
                    }
                    print("onRequestPermissionsResult: permission granted")
                    mLocationPermissionsGranted = true
                    initMap()
                }
            }
        }
    }

    private fun addMarkersToMapCar() {

        val placeDetailsMap = mutableMapOf(
                // Uses a coloured icon
                "PERTH" to PlaceDetails(
                        position = places.getValue("PERTH"),
                        title = "Perth",
                        snippet = "รายละเอียด"
                ),


                "SYDNEY" to PlaceDetails(
                        position = places.getValue("SYDNEY"),
                        title = "Central rama9",
                        snippet = "รายละเอียด"
                ),
                "rat2" to PlaceDetails(
                        position = places.getValue("rat2"),
                        title = "rat2",
                        snippet = "รายละเอียด"
                )

        )
        placeDetailsMap.keys.map {
            with(placeDetailsMap.getValue(it)) {
                mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                        .position(position)
                        .title(title)
                        .snippet(snippet)
                        .icon(icon)
                )
            }
        }
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        showParkingDetail()
        return false
    }

    private fun setupButtonBack(){
        buttonBack.setOnClickListener {
            hideParkingDetail()
        }
    }

    private fun showParkingDetail() {
        TransitionManager.beginDelayedTransition(layoutActivityMaps)
        layout_detail.visibility = View.VISIBLE
        buttonBack.visibility = View.VISIBLE
        relLayout1.visibility = View.GONE
        spinner.visibility = View.GONE
        mMap.setPadding(0, 0, 0, layout_detail.height)
    }


    private fun hideParkingDetail() {
        TransitionManager.beginDelayedTransition(layoutActivityMaps)
        layout_detail.setVisibility(View.GONE)
        buttonBack.visibility = View.GONE
        relLayout1.visibility = View.VISIBLE
        spinner.visibility = View.VISIBLE
        mMap.setPadding(0, 0, 0, 0)

    }
}

class PlaceDetails(
        val position: LatLng,
        val title: String = "Marker",
        val snippet: String? = null,
        val icon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker())





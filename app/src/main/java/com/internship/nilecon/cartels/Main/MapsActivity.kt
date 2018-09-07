package com.internship.nilecon.cartels.Main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceBufferResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.internship.nilecon.cartels.R
import com.internship.nilecon.cartels.SplashScreen.SplashScreenActivity
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener
        , GoogleMap.OnMarkerClickListener {


    //search

    lateinit var mGeoDataClient: GeoDataClient
    lateinit var placesAdapter: PlacesAdapter
    lateinit var latLng: LatLng
    lateinit var mLocationRequest: LocationRequest
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var mLocationCallback: LocationCallback
    lateinit var mSettingsClient: SettingsClient
    lateinit var mLocationSettingsRequest: LocationSettingsRequest
    var isAutoCompleteLocation = false
    val BOUNDS_Lng = LatLngBounds(LatLng(5.371270, 97.859916), LatLng(19.680066, 104.957083))

    private var home_location: Location? = null
    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 777
    private lateinit var mMap: GoogleMap
    private var mLocationPermissionsGranted: Boolean? = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
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
        MapsInitializer.initialize(this)
        mGeoDataClient = Places.getGeoDataClient(this, null)

        LocationRequest()
        setupButtonBack()
        setupButtonMylocation()
        setupLocationPermission()
        CallDirections()
        CallnNumber()


    }

    override fun onMapReady(googleMap: GoogleMap) {
        val TAG = "Main"
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false
        try {
            val success : Boolean = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this , R.raw.map))

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e : Resources.NotFoundException){
               Log.e(TAG, "Can't find style. Error: ", e)
        }


        mMap.setPadding(48, 0, 0, 24)

        if (mLocationPermissionsGranted!!) {

            getDeviceLocation()

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = false

        }
        with(mMap) {

            this!!.setOnMarkerClickListener(this@MapsActivity)
        }
        addMarkersToMapCar()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        showParkingDetail()
        return false
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

    override fun onBackPressed() {
        var perfs = this.getSharedPreferences(getString(R.string.app_name)/*ตั้งชื่อของ SharedPreferences*/
                , Context.MODE_PRIVATE/*SharedPreferences แบบเห็นได้เฉพาะ app นี้เท่านั้น MODE_PRIVATE*/)
                .edit()  // ประกาศใช้ SharedPreferences เพื่อลบ Token
        perfs.clear()
        perfs.commit() /*ยืนยันการบันทึก SharedPreferences*/

        val intent = Intent(this, SplashScreenActivity::class.java)
        startActivity(intent)

        finishAffinity()
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
        print("moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude)
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

    private fun setupButtonBack() {
        buttonBack.setOnClickListener {
            hideParkingDetail()
        }
    }

    private fun setupButtonMylocation() {
        buttonMyLocation.setOnClickListener {
            getDeviceLocation()
        }
    }

    private fun showParkingDetail() {
        TransitionManager.beginDelayedTransition(layoutActivityMaps)
        constraintLayoutDetail.visibility = View.VISIBLE
        buttonCall.visibility = View.VISIBLE
        buttonDirections.visibility = View.VISIBLE
        constraintLayoutActionBar.visibility = View.GONE
        buttonBack.visibility = View.VISIBLE
        spinnerFilterVehicle.visibility = View.GONE
        mMap.setPadding(48, 0, 0, constraintLayoutDetail.height + buttonCall.height + 112)
    }

    private fun hideParkingDetail() {
        TransitionManager.beginDelayedTransition(layoutActivityMaps)
        constraintLayoutDetail.visibility = View.GONE
        buttonBack.visibility = View.GONE
        buttonCall.visibility = View.GONE
        buttonDirections.visibility = View.GONE
        constraintLayoutActionBar.visibility = View.VISIBLE
        spinnerFilterVehicle.visibility = View.VISIBLE
        mMap.setPadding(48, 0, 0, 24)

    }

    fun hideKeyboard() {
        try {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //search
    private fun CallnNumber() {
        buttonCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0123456789")
            startActivity(intent)
        }
    }

    private fun CallDirections() {
        buttonDirections.setOnClickListener {

            val url = "https://www.google.com/maps/dir/Current+Location/760+West+Genesee+Street+Syracuse+NY+13204&mode=driving"
            val gmmIntentUri = Uri.parse(url)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)

        }

    }

    private fun LocationRequest() {

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val loc = locationResult!!.lastLocation
                if (!isAutoCompleteLocation) {
                    home_location = loc
                    latLng = LatLng(home_location!!.latitude, home_location!!.longitude)
                    assignToMap()
                }
            }
        }

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval((10 * 1000).toLong())        // 10 seconds, in milliseconds
                .setFastestInterval((6 * 1000).toLong()) // 1 second, in milliseconds


        mSettingsClient = LocationServices.getSettingsClient(this)
        val builder = LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest)
        mLocationSettingsRequest = builder.build()

        placesAdapter = PlacesAdapter(this, android.R.layout.simple_expandable_list_item_2, mGeoDataClient, null, BOUNDS_Lng)
        autoCompleteTextViewSearch.setAdapter(placesAdapter)
        autoCompleteTextViewSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                buttonMenu.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    cancel.visibility = View.VISIBLE

                } else {
                    cancel.visibility = View.GONE
                }
            }
        })
        autoCompleteTextViewSearch.setOnItemClickListener { parent, view, position, id ->
            //getLatLong(placesAdapter.getPlace(position))
            hideKeyboard()
            val item = placesAdapter.getItem(position)
            val placeId = item!!.placeId
            val primaryText = item.getPrimaryText(null)

            Log.i("Autocomplete", "Autocomplete item selected: $primaryText")

            val placeResult = mGeoDataClient.getPlaceById(placeId)
            placeResult.addOnCompleteListener(object : OnCompleteListener<PlaceBufferResponse> {
                override fun onComplete(task: Task<PlaceBufferResponse>) {
                    val places = task.result
                    val place = places.get(0)

                    isAutoCompleteLocation = true
                    latLng = place.latLng

                    assignToMap()

                    places.release()
                }
            })
        }

        cancel.setOnClickListener {
            autoCompleteTextViewSearch.setText("")
        }
    }

    private fun assignToMap() {

        mMap.clear()
        val options = MarkerOptions()
                .position(latLng)
                .title("My Location")
        mMap.apply {
            addMarker(options)
            moveCamera(CameraUpdateFactory.newLatLng(latLng))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        }
        Toast.makeText(applicationContext, "Clicked: " + latLng.toString(),
                Toast.LENGTH_SHORT).show()

    }
}

class PlaceDetails(
        val position: LatLng,
        val title: String = "Marker",
        val snippet: String? = null,
        val icon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker())





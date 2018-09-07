package com.internship.nilecon.cartels.Main

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.*
import com.internship.nilecon.cartels.API.Api
import com.internship.nilecon.cartels.API.ParkingForGetParkingPointByLatLngDTO
import com.internship.nilecon.cartels.API.ParkingPoint
import com.internship.nilecon.cartels.API.ParkingsInterface
import com.internship.nilecon.cartels.R
import com.internship.nilecon.cartels.SplashScreen.SplashScreenActivity
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener
        , GoogleMap.OnMarkerClickListener{

    private val LOCATION_PERMISSION_REQUEST_CODE = 777
    private lateinit var mMap: GoogleMap
    private var mLocationPermissionsGranted: Boolean? = false
    private var mApi : Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        MapsInitializer.initialize(this)

        setupSearch()
        setupButtonBack()
        setupButtonMylocation()
        setupLocationPermission()
        setupButtonDirections()
        setupButtonCall()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
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
        //addMarkersToMapCar()
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

    private fun callApiGetParkingPointByLatLng(parkingForGetParkingPointByLatLngDTO: ParkingForGetParkingPointByLatLngDTO){

        val prefs = getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE)
        var token  = prefs.getString("Token",null)

        mApi = Api().Declaration(this,ParkingsInterface::class.java)
                .getParkingPointByLatLng("Bearer $token",parkingForGetParkingPointByLatLngDTO)

        (mApi as Call<List<ParkingPoint>>).enqueue(object : Callback<List<ParkingPoint>>{
            override fun onFailure(call: Call<List<ParkingPoint>>?, t: Throwable?) {
                print(t!!.message)
            }

            override fun onResponse(call: Call<List<ParkingPoint>>?, response: Response<List<ParkingPoint>>?) {
                when(response!!.code()){
                    200 -> {
                        addMarkersToMapCar(response.body()!!)
                    }
                    404 ->{
                        Toast.makeText(this@MapsActivity,"Not found",Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun getDeviceLocation() {
        print("getDeviceLocation: getting the devices current location")
        var mFusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val DEFAULT_ZOOM = 16f

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
        val permission = Manifest.permission.ACCESS_COARSE_LOCATION
        val location = Manifest.permission.ACCESS_FINE_LOCATION

        print("setupLocationPermission: getting location permissions")

        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (ContextCompat.checkSelfPermission(this.applicationContext,
                        location) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.applicationContext,
                            permission) == PackageManager.PERMISSION_GRANTED) {
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

    private fun addMarkersToMapCar(parkingPointList : List<ParkingPoint> ) {
        for(i in 0 until parkingPointList.size){
            when(parkingPointList[i].Type){
                "Other" -> {
                    mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                            .position(LatLng(parkingPointList[i].Latitude!!, parkingPointList[i].Longitude!!))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_pubilc))
                    )
                }
                "Home" -> {
                    mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                            .position(LatLng(parkingPointList[i].Latitude!!, parkingPointList[i].Longitude!!))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_home))
                    )
                }
                "Building" ->{
                    mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                            .position(LatLng(parkingPointList[i].Latitude!!, parkingPointList[i].Longitude!!))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_building))
                    )
                }
            }
        }
    }

        /*val placeDetailsMap = mutableMapOf(
                // Uses a coloured icon
                "PERTH" to PlaceDetails(
                        position = LatLng(13.7748604, 100.5745226),
                        title = "Perth",
                        snippet = "รายละเอียด"
                ),


                "SYDNEY" to PlaceDetails(
                        position = LatLng(13.7741698, 100.5754419),
                        title = "Central rama9",
                        snippet = "รายละเอียด"
                ),
                "rat2" to PlaceDetails(
                        position =LatLng(13.7803506, 100.5748004),
                        title = "rat2",
                        snippet = "รายละเอียด"
                )

        )
        placeDetailsMap.keys.map {
            with(placeDetailsMap.getValue(it)) {
                mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                        .position(position)
                        .icon(icon)
                )
            }
        }*/

        /*mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                .position(position)
                .icon(icon)*/


    private fun setupButtonBack() {
        buttonBack.setOnClickListener {
            hideParkingDetail()
        }
    }

    private fun setupButtonDirections() {
        buttonDirections.setOnClickListener {
            val url = "https://www.google.com/maps/dir/Current+Location/760+West+Genesee+Street+Syracuse+NY+13204&mode=driving"
            val gmmIntentUri = Uri.parse(url)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

    }

    private fun setupButtonCall() {
        buttonCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:0123456789")
            startActivity(intent)
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

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    private fun setupSearch() {
        lateinit var searchAdapter: SearchAdapter

        val asiaZone = LatLngBounds(LatLng(5.371270, 97.859916), LatLng(19.680066, 104.957083))
        lateinit var latLng: LatLng
        var mGeoDataClient: GeoDataClient = Places.getGeoDataClient(this, null)
        var mLocationRequest: LocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval((10 * 1000).toLong())        // 10 seconds, in milliseconds
                .setFastestInterval((6 * 1000).toLong()) // 6 second, in milliseconds

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)

        searchAdapter = SearchAdapter(this, R.layout.expandable_list_item, mGeoDataClient, null, asiaZone)
        autoCompleteTextViewSearch.setAdapter(searchAdapter)
        autoCompleteTextViewSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                buttonMenu.visibility = View.VISIBLE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count > 0) {
                    buttonCancel.visibility = View.VISIBLE

                } else {
                    buttonCancel.visibility = View.GONE
                }
            }
        })
        autoCompleteTextViewSearch.setOnItemClickListener { parent, view, position, id ->
            hideKeyboard()
            val item = searchAdapter.getItem(position)
            val placeId = item!!.placeId
            val primaryText = item.getPrimaryText(null)

            Log.i("Autocomplete", "Autocomplete item selected: $primaryText")

            val placeResult = mGeoDataClient.getPlaceById(placeId)

            placeResult.addOnCompleteListener { task ->
                val places = task.result
                val place = places.get(0)
                latLng = place.latLng
                assignToMap(latLng)
            }
        }

        buttonCancel.setOnClickListener {
            autoCompleteTextViewSearch.setText("")
        }
    }

    private fun assignToMap(latLng: LatLng) {
        mMap.apply {
            moveCamera(CameraUpdateFactory.newLatLng(latLng))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
        callApiGetParkingPointByLatLng(ParkingForGetParkingPointByLatLngDTO(latLng.latitude,latLng.longitude,1,"All"))
    }

}

class PlaceDetails(
        val position: LatLng,
        val title: String = "Marker",
        val snippet: String? = null,
        val icon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker())





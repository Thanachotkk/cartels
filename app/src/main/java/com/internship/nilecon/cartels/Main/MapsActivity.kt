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
import android.transition.Explode
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.*
import com.internship.nilecon.cartels.API.*
import com.internship.nilecon.cartels.R
import com.internship.nilecon.cartels.SplashScreen.SplashScreenActivity
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity()
        , OnMapReadyCallback
        , GoogleApiClient.OnConnectionFailedListener
        , GoogleMap.OnMarkerClickListener {


    private val LOCATION_PERMISSION_REQUEST_CODE = 777
    private lateinit var mMap: GoogleMap
    private var myLocation: Location? = null
    private var mLocationPermissionsGranted: Boolean? = false
    private var mApi: Any? = null
    private var VehicleType = "All"

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
        setupSpinnerFilterVehicle()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setPadding(48, 0, 48, 0)

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

        onCameraChangeListener()

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        callApiGetParkingDetail(ParkingForGetParkingDetailDTO(marker!!.tag as Int))
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

    private fun onCameraChangeListener() {
        mMap.setOnCameraChangeListener {
            mMap.clear()
            callApiGetParkingPointByLatLng(ParkingForGetParkingPointByLatLngDTO(
                    it.target.latitude,
                    it.target.longitude,
                    1,
                    VehicleType))
            mMap.addCircle(CircleOptions()
                    .center(LatLng(it.target.latitude, it.target.longitude))
                    .radius(1000.0)
                    .strokeColor(resources.getColor(R.color.colorTheme3))
                    .fillColor(resources.getColor(R.color.colorTheme3_opacity20)))
        }
    }

    private fun callApiGetParkingPointByLatLng(parkingForGetParkingPointByLatLngDTO: ParkingForGetParkingPointByLatLngDTO) {
        val prefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        var token = prefs.getString("Token", null)

        mApi = Api().Declaration(this, ParkingsInterface::class.java)
                .getParkingPointByLatLng("Bearer $token", parkingForGetParkingPointByLatLngDTO)

        (mApi as Call<List<ParkingPoint>>).enqueue(object : Callback<List<ParkingPoint>> {
            override fun onFailure(call: Call<List<ParkingPoint>>?, t: Throwable?) {
                print(t!!.message)
            }

            override fun onResponse(call: Call<List<ParkingPoint>>?, response: Response<List<ParkingPoint>>?) {
                when (response!!.code()) {
                    200 -> {
                        addMarkersToMapCar(response.body()!!)
                    }
                    404 -> {

                    }
                }
            }
        })
    }

    private fun callApiGetParkingDetail(parkingForGetParkingDetailDTO: ParkingForGetParkingDetailDTO) {

        val prefs = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        var token = prefs.getString("Token", null)

        mApi = Api().Declaration(this, ParkingsInterface::class.java)
                .getParkingDetail("Bearer $token", parkingForGetParkingDetailDTO)

        (mApi as Call<ParkingDetail>).enqueue(object : Callback<ParkingDetail> {
            override fun onFailure(call: Call<ParkingDetail>, t: Throwable) {
                print(t.message)
            }

            override fun onResponse(call: Call<ParkingDetail>, response: Response<ParkingDetail>) {
                when (response.code()) {
                    200 -> {
                        showParkingDetail(response.body())
                    }
                    404 -> {

                    }
                }
            }
        })
    }

    private fun getDeviceLocation() {
        print("getDeviceLocation: getting the devices current location")
        var mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val DEFAULT_ZOOM = 14.5f

        try {
            if (mLocationPermissionsGranted!!) {

                val location = mFusedLocationProviderClient!!.lastLocation
                location.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        print("onComplete: found location!")
                        val currentLocation = task.result as Location
                        myLocation = currentLocation
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

    private fun addMarkersToMapCar(parkingPointList: List<ParkingPoint>) {
        for (i in 0 until parkingPointList.size) {
            when (parkingPointList[i].Type) {
                "Other" -> {
                    var marker = mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                            .position(LatLng(parkingPointList[i].Latitude!!, parkingPointList[i].Longitude!!))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_pubilc))
                    )

                    marker.tag = parkingPointList[i].ParkingId
                }
                "Home" -> {
                    var marker = mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                            .position(LatLng(parkingPointList[i].Latitude!!, parkingPointList[i].Longitude!!))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_home))

                    )
                    marker.tag = parkingPointList[i].ParkingId
                }
                "Building" -> {
                    var marker = mMap!!.addMarker(com.google.android.gms.maps.model.MarkerOptions()
                            .position(LatLng(parkingPointList[i].Latitude!!, parkingPointList[i].Longitude!!))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_building))

                    )
                    marker.tag = parkingPointList[i].ParkingId
                }
            }
        }
    }

    private fun setupSpinnerFilterVehicle() {
        val spinnerFilterVehicleAdapter = SpinnerFilterVehicleAdapter(this)

        val spinnerFilterVehicleList = ArrayList<SpinnerFilterVehicle>()
        spinnerFilterVehicleList.add(SpinnerFilterVehicle(R.drawable.ic_local_parking_black_24dp, "All"))
        spinnerFilterVehicleList.add(SpinnerFilterVehicle(R.drawable.ic_car_black_24dp, "Car"))
        spinnerFilterVehicleList.add(SpinnerFilterVehicle(R.drawable.ic_directions_bike_black_24dp, "Motorcycle"))
        spinnerFilterVehicleList.add(SpinnerFilterVehicle(R.drawable.ic_motorcycle_black_24dp, "Bigbike"))

        spinnerFilterVehicleAdapter.setSpinnerFilterVehicleList(spinnerFilterVehicleList)

        spinnerFilterVehicle.adapter = spinnerFilterVehicleAdapter

        spinnerFilterVehicle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mMap.clear()
                VehicleType = spinnerFilterVehicleList[position].textViewVehicleType
                callApiGetParkingPointByLatLng(ParkingForGetParkingPointByLatLngDTO(
                        mMap.cameraPosition.target.latitude,
                        mMap.cameraPosition.target.longitude,
                        1,
                        VehicleType))
                mMap.addCircle(CircleOptions()
                        .center(LatLng(mMap.cameraPosition.target.latitude, mMap.cameraPosition.target.longitude))
                        .radius(1000.0)
                        .strokeColor(resources.getColor(R.color.colorTheme3))
                        .fillColor(resources.getColor(R.color.colorTheme3_opacity20)))
            }
        }
    }

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

    private fun showParkingDetail(parkingDetail: ParkingDetail?) {
        TransitionManager.beginDelayedTransition(layoutActivityMaps,Explode())

        when (parkingDetail!!.Type) {
            "Other" -> {
                constraintLayoutDetail.visibility = View.VISIBLE
                constraintLayoutActionBar.visibility = View.GONE
                buttonBack.visibility = View.VISIBLE
                spinnerFilterVehicle.visibility = View.GONE
                mMap.setPadding(48, 0, 48, constraintLayoutDetail.height + buttonCall.height + 112)

                textViewTitle.text = parkingDetail.Title;textViewTitle.isSelected = true

                var parkingLocation = Location("")
                parkingLocation.apply {
                    latitude = parkingDetail.Latitude!!
                    longitude = parkingDetail.Longitude!!
                }

                textViewDistance.text = (myLocation!!.distanceTo(parkingLocation)/1000).toString()

                textViewAddressValue.text = parkingDetail.Address;textViewAddressValue.isSelected = true

                if (parkingDetail.OpenTime == parkingDetail.CloseTime) {
                    textViewOpenAndCloseValue.text = "24 hour";textViewOpenAndCloseValue.isSelected = true
                } else {
                    textViewOpenAndCloseValue.text = parkingDetail.OpenTime!!.substring(0, 5) + " - " + parkingDetail.CloseTime!!.substring(0, 5);textViewOpenAndCloseValue.isSelected = true
                }

                parkingDetail.Supports!!.forEachIndexed { index, s ->
                    if (index == 0) textViewSupportValue.text = s
                    else textViewSupportValue.text = textViewSupportValue.text.toString() + ", $s"
                };textViewSupportValue.isSelected = true

            }
            "Home" -> {
                constraintLayoutDetail.visibility = View.VISIBLE
                constraintLayoutActionBar.visibility = View.GONE
                buttonBack.visibility = View.VISIBLE
                spinnerFilterVehicle.visibility = View.GONE
                mMap.setPadding(48, 0, 48, constraintLayoutDetail.height + buttonCall.height + 112)

                textViewTitle.text = parkingDetail.Title;textViewTitle.isSelected = true

                var parkingLocation = Location("")
                parkingLocation.apply {
                    latitude = parkingDetail.Latitude!!
                    longitude = parkingDetail.Longitude!!
                }

                textViewDistance.text = (myLocation!!.distanceTo(parkingLocation)/1000).toString()

                textViewAddressValue.text = parkingDetail.Address;textViewAddressValue.isSelected = true

                if (parkingDetail.OpenTime == parkingDetail.CloseTime) {
                    textViewOpenAndCloseValue.text = "24 hour";textViewOpenAndCloseValue.isSelected = true
                } else {
                    textViewOpenAndCloseValue.text = parkingDetail.OpenTime!!.substring(0, 5) + " - " + parkingDetail.CloseTime!!.substring(0, 5);textViewOpenAndCloseValue.isSelected = true
                }

                parkingDetail.Supports!!.forEachIndexed { index, s ->
                    if (index == 0) textViewSupportValue.text = s
                    else textViewSupportValue.text = textViewSupportValue.text.toString() + ", $s"
                };textViewSupportValue.isSelected = true
            }
            "Building" -> {
                constraintLayoutDetail.visibility = View.VISIBLE
                constraintLayoutActionBar.visibility = View.GONE
                buttonBack.visibility = View.VISIBLE
                spinnerFilterVehicle.visibility = View.GONE
                mMap.setPadding(48, 0, 48, constraintLayoutDetail.height + buttonCall.height + 112)

                textViewTitle.text = parkingDetail.Title;textViewTitle.isSelected = true

                var parkingLocation = Location("")
                parkingLocation.apply {
                    latitude = parkingDetail.Latitude!!
                    longitude = parkingDetail.Longitude!!
                }
                textViewDistance.text = (myLocation!!.distanceTo(parkingLocation)/1000).toString()

                textViewAddressValue.text = parkingDetail.Address;textViewAddressValue.isSelected = true

                if (parkingDetail.OpenTime == parkingDetail.CloseTime) {
                    textViewOpenAndCloseValue.text = "24 hour";textViewOpenAndCloseValue.isSelected = true
                } else {
                    textViewOpenAndCloseValue.text = parkingDetail.OpenTime!!.substring(0, 5) + " - " + parkingDetail.CloseTime!!.substring(0, 5);textViewOpenAndCloseValue.isSelected = true
                }

                parkingDetail.Supports!!.forEachIndexed { index, s ->
                    if (index == 0) textViewSupportValue.text = s
                    else textViewSupportValue.text = textViewSupportValue.text.toString() + ", $s"
                };textViewSupportValue.isSelected = true
            }
        }


    }

    private fun hideParkingDetail() {
        TransitionManager.beginDelayedTransition(layoutActivityMaps,Explode())
        constraintLayoutDetail.visibility = View.GONE
        buttonBack.visibility = View.GONE
        constraintLayoutActionBar.visibility = View.VISIBLE
        spinnerFilterVehicle.visibility = View.VISIBLE
        mMap.setPadding(48, 0, 48, 24)

    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    private fun cancleApi() {
        if (mApi != null) { // ถ้า Api request ยังไม่สำเร็จ
            (mApi as Call<*>).cancel() //ยกเลิก Api request
        }
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
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.5f))
        }
    }

}





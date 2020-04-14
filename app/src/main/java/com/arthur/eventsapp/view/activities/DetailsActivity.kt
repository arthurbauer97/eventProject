package com.arthur.eventsapp.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources.NotFoundException
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import com.arthur.eventsapp.R
import com.arthur.eventsapp.view.fragments.WorkaroundMapFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var PERMISSION_ID = 42
    lateinit var mMap: GoogleMap
    lateinit var myMarkerLatLng: LatLng
    lateinit var nestedScrollView: NestedScrollView
    private var builder: LatLngBounds.Builder = LatLngBounds.Builder()

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val mapFragment: SupportMapFragment? = supportFragmentManager
            .findFragmentById(R.id.map) as WorkaroundMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()

        tv_descricaoEvento.setText(intent.getStringExtra("descricaoEvento"));
        tv_nomeEvento.setText(intent.getStringExtra("nomeEvento"));


        img_back.setOnClickListener {
            onBackPressed()
        }


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // personaliza o mapa com o tom preto de fundo
        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style
                )
            )
            if (!success) {
                Log.e("style_map", "Style parsing failed.")
            }
        } catch (e: NotFoundException) {
            Log.e("style_map", "Can't find style. Error: ", e)
        }

        // Add a marker in Sydney, Australia, and move the camera.
        val sydney =
            LatLng(intent.getDoubleExtra("latEvento", 0.0), intent.getDoubleExtra("lonEvento", 0.0))

        mMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title(intent.getStringExtra("cidadeEvento"))
                .snippet(intent.getStringExtra("localEvento"))
        )
        builder.include(sydney)

        nestedScrollView =
            findViewById<NestedScrollView>(R.id.nestedScrollView) //parent scrollview in xml, give your scrollview id value

        (supportFragmentManager.findFragmentById(R.id.map) as WorkaroundMapFragment?)
            ?.setListener(object : WorkaroundMapFragment.OnTouchListener {

                override fun onTouch() {
                    nestedScrollView.requestDisallowInterceptTouchEvent(true)
                }
            })

    }

    fun myMarker(lat: Double, lon: Double) {

        val myMarker = LatLng(lat, lon)
        mMap.addMarker(
            MarkerOptions()
                .position(myMarker)
                .title(intent.getStringExtra("Sua Localizacao"))
        )
        builder.include(myMarker)

        val bounds: LatLngBounds = builder.build()
//        val width = getResources().getDisplayMetrics().widthPixels
//        val height = getResources().getDisplayMetrics().heightPixels
        val padding = 100
        val cu = CameraUpdateFactory.newLatLngBounds(bounds,padding)
        mMap.moveCamera(cu)
    }

    //----------------------------------PERMISSIONS AND REQUESTS------------------------------------
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        location.latitude
                        location.longitude
                        myMarker(location.latitude, location.longitude)

                        //distance(location.latitude,location.longitude,intent.getDoubleExtra("lat",0.0),intent.getDoubleExtra("long",0.0))
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
        }
    }


    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}
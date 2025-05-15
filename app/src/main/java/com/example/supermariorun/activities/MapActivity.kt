package com.example.supermariorun.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.supermariorun.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map_button_close: ImageButton
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        map_button_close = findViewById(R.id.map_button_close)

        latitude = intent.getDoubleExtra("LAT", 0.0)
        longitude = intent.getDoubleExtra("LON", 0.0)

        findViewById<ImageButton>(R.id.map_button_close).setOnClickListener {
            finish()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val location = LatLng(latitude, longitude)

        // Add a real Google Maps marker with lat/lon label as title/snippet
        map?.addMarker(
            MarkerOptions()
                .position(location)
                .title("High Score Location")
                .snippet("📍 %.2f, %.2f".format(latitude, longitude))
        )?.showInfoWindow()

        // Center the camera on the actual point
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }


}

package com.example.supermariorun.utilities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class LocationFetcher(private val activity: Activity) {

    private val LOCATION_PERMISSION_REQUEST_CODE = 101
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

    fun requestLocation(onResult: (lat: Double, lon: Double) -> Unit) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("LocationFetcher", "Permission not granted, requesting...")
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // ✅ נסה לקבל מיקום עדכני
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            Log.d("LocationFetcher", "getCurrentLocation triggered")
            if (location != null) {
                Log.d("LocationFetcher", "Lat: ${location.latitude}, Lon: ${location.longitude}")
                onResult(location.latitude, location.longitude)
            } else {
                Log.w("LocationFetcher", "Location is null from getCurrentLocation")

                // ⛑ fallback - ננסה עם requestLocationUpdates
                requestLocationWithCallback(onResult)
            }
        }.addOnFailureListener {
            Log.e("LocationFetcher", "Failed to get location: ${it.message}")
            requestLocationWithCallback(onResult)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun requestLocationWithCallback(onResult: (lat: Double, lon: Double) -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                Log.d("LocationFetcher", "Fallback callback triggered")
                Log.d("LocationFetcher", "Location object: $location")

                if (location != null) {
                    Log.d("LocationFetcher", "Lat: ${location.latitude}, Lon: ${location.longitude}")
                    onResult(location.latitude, location.longitude)
                } else {
                    Log.w("LocationFetcher", "Location is null in fallback callback")
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}

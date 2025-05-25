package com.example.supermariorun.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.supermariorun.MainActivity
import com.example.supermariorun.R
import com.example.supermariorun.data.GameData
import com.example.supermariorun.utilities.LocationFetcher

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_switch_mode: SwitchCompat
    private lateinit var switchTiltMode: SwitchCompat
    private lateinit var menu_btn_play: Button
    private lateinit var menu_btn_high_scores: Button
    private lateinit var locationFetcher: LocationFetcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        locationFetcher = LocationFetcher(this)

        locationFetcher.requestLocation { lat, lon ->
            GameData.latitude = lat
            GameData.longitude = lon
            Log.d("MenuActivity", "Location ready: $lat, $lon")
        }

        menu_switch_mode = findViewById(R.id.menu_switch_mode)
        switchTiltMode = findViewById(R.id.switchTiltMode)
        menu_btn_play = findViewById(R.id.menu_btn_play)
        menu_btn_high_scores = findViewById(R.id.menu_btn_high_scores)

        menu_btn_play.setOnClickListener {
            val fastMode = menu_switch_mode.isChecked
            val useTilt = switchTiltMode.isChecked

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("MODE_SENSOR", fastMode)
            intent.putExtra("USE_TILT", useTilt)

            startActivity(intent)
        }

        menu_btn_high_scores.setOnClickListener {
            val intent = Intent(this, HighScoresActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            locationFetcher.requestLocation { lat, lon ->
                GameData.latitude = lat
                GameData.longitude = lon
                Log.d("MenuActivity", "Permission granted, location updated: $lat, $lon")
            }
        } else {
            Log.w("MenuActivity", "Location permission denied by user")
        }
    }
}

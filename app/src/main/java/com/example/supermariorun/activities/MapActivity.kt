package com.example.supermariorun.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.supermariorun.R

class MapActivity : AppCompatActivity() {

    private lateinit var map_text_location: TextView
    private lateinit var map_button_close: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        map_text_location = findViewById(R.id.map_text_location)
        map_button_close = findViewById(R.id.map_button_close)

        val lat = intent.getDoubleExtra("LAT", 0.0)
        val lon = intent.getDoubleExtra("LON", 0.0)

        map_text_location.text = "📍\n$lat\n$lon"

        map_button_close.setOnClickListener {
            finish()
        }
    }
}

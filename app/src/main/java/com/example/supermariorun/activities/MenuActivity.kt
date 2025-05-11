package com.example.supermariorun.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.supermariorun.MainActivity
import com.example.supermariorun.R
//import com.example.supermariorun.activities.HighScoresActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var menu_switch_mode: Switch
    private lateinit var menu_btn_play: Button
    private lateinit var menu_btn_high_scores: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        menu_switch_mode = findViewById(R.id.menu_switch_mode)
        menu_btn_play = findViewById(R.id.menu_btn_play)
        menu_btn_high_scores = findViewById(R.id.menu_btn_high_scores)

        menu_btn_play.setOnClickListener {
            val useSensor = menu_switch_mode.isChecked
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("MODE_SENSOR", useSensor)
            startActivity(intent)
        }

        menu_btn_high_scores.setOnClickListener {
            val intent = Intent(this, HighScoresActivity::class.java)
            startActivity(intent)
        }
    }
}

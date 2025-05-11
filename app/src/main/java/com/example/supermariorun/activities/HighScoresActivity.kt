package com.example.supermariorun.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.supermariorun.R
import com.example.supermariorun.data.HighScore
import com.example.supermariorun.utilities.HighScoreManager
import com.example.supermariorun.MapActivity

class HighScoresActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HighScoresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        recyclerView = findViewById(R.id.high_scores_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val scores: List<HighScore> = HighScoreManager.loadHighScores(this)
        adapter = HighScoresAdapter(scores) { score ->
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("LAT", score.lat)
            intent.putExtra("LON", score.lon)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }
}

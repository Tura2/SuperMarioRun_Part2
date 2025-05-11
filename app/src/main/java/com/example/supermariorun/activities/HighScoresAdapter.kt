package com.example.supermariorun.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supermariorun.R
import com.example.supermariorun.data.HighScore

class HighScoresAdapter(
    private val scores: List<HighScore>,
    private val onMapClick: (HighScore) -> Unit
) : RecyclerView.Adapter<HighScoresAdapter.HighScoreViewHolder>() {

    class HighScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.item_txt_name)
        val coinsText: TextView = itemView.findViewById(R.id.item_txt_coins)
        val metersText: TextView = itemView.findViewById(R.id.item_txt_meters)
        val mapButton = itemView.findViewById<ImageButton>(R.id.item_btn_map)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_high_score, parent, false)
        return HighScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        val score = scores[position]
        holder.nameText.text = score.name
        holder.coinsText.text = "Coins: ${score.coins}"
        holder.metersText.text = "Meters: ${score.meters}"
        holder.mapButton.setOnClickListener {
            onMapClick(score)
        }
    }

    override fun getItemCount(): Int = scores.size
}

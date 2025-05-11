package com.example.supermariorun.utilities

import android.widget.ImageView
import android.widget.TextView
import com.example.supermariorun.R

class UIUpdater(
    private val heart1: ImageView,
    private val heart2: ImageView,
    private val heart3: ImageView,
    private val metersTextView: TextView,
    private val coinsTextView: TextView
) {
    fun updateHearts(livesLeft: Int) {
        when (livesLeft) {
            3 -> {
                heart1.setImageResource(R.drawable.heart)
                heart2.setImageResource(R.drawable.heart)
                heart3.setImageResource(R.drawable.heart)
            }
            2 -> heart3.setImageDrawable(null)
            1 -> heart2.setImageDrawable(null)
            0 -> heart1.setImageDrawable(null)
        }
    }

    fun updateMeters(meters: Int) {
        metersTextView.text = "Meters: $meters"
    }

    fun updateCoins(coins: Int) {
        coinsTextView.text = "Coins: $coins"
    }
}

package com.example.supermariorun.data



data class HighScore(
    val name: String,
    val coins: Int,
    val meters: Int,
    val lat: Double,
    val lon: Double
)

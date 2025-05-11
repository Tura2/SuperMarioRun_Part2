package com.example.supermariorun.utilities

import android.content.Context
import android.content.SharedPreferences
import com.example.supermariorun.data.HighScore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object HighScoreManager {

    private const val PREFS_NAME = "high_scores_prefs"
    private const val KEY_SCORES = "high_scores_list"

    private val gson = Gson()
    private val typeToken: java.lang.reflect.Type =
        object : TypeToken<MutableList<HighScore>>() {}.type

    fun loadHighScores(context: Context): List<HighScore> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_SCORES, null)
        return if (json != null) gson.fromJson(json, typeToken) else emptyList()
    }

    fun saveHighScores(context: Context, scores: List<HighScore>) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = gson.toJson(scores)
        prefs.edit().putString(KEY_SCORES, json).apply()
    }

    fun addHighScore(context: Context, newScore: HighScore) {
        val current = loadHighScores(context).toMutableList()
        current.add(newScore)
        current.sortByDescending { it.coins }
        if (current.size > 10) current.removeLast()
        saveHighScores(context, current)
    }
}

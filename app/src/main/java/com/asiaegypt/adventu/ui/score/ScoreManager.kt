package com.asiaegypt.adventu.ui.score

import android.content.SharedPreferences
import com.asiaegypt.adventu.model.ScoreStats

object ScoreManager {

    val stats = mutableMapOf(
        "Easy" to ScoreStats(),
        "Medium" to ScoreStats(),
        "Hard" to ScoreStats()
    )

    fun saveStatsScoreFindPairGame(preferences: SharedPreferences) {
        val editor = preferences.edit()
        for ((level, stats) in stats) {
            editor.putLong("${level}_bestTime", stats.bestTime)
            editor.putInt("${level}_bestSteps", stats.bestSteps)
        }
        editor.apply()
    }

    fun loadStatsScoreFindPairGame(sharedPref: SharedPreferences) {
        for ((level, stats) in stats) {
            stats.bestTime = sharedPref.getLong("${level}_bestTime", Long.MAX_VALUE)
            stats.bestSteps = sharedPref.getInt("${level}_bestSteps", 0)
        }
    }
}
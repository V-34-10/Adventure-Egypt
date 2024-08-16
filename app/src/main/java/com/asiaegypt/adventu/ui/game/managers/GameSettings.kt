package com.asiaegypt.adventu.ui.game.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

object GameSettings {
    private lateinit var preferences: SharedPreferences

    var selectedLevel: String = ""
        private set
    var selectedTheme: String = ""
        private set

    fun init(context: Context) {
        preferences = getPreference(context)
        selectedLevel = preferences.getString("levelFindPair", "") ?: ""
        selectedTheme = preferences.getString("themeFindPair", "") ?: ""
    }

    fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            "AsianEgyptAdventurePref",
            AppCompatActivity.MODE_PRIVATE
        )
    }
}
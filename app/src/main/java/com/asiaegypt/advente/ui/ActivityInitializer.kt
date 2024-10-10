package com.asiaegypt.advente.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.advente.NavigationManager
import com.asiaegypt.advente.R
import com.asiaegypt.advente.ui.settings.MusicManager
import com.asiaegypt.advente.ui.settings.MusicStart

class ActivityInitializer(private val activity: Activity) {

    val preferences by lazy {
        activity.getSharedPreferences(
            "AsianEgyptAdventurePref",
            AppCompatActivity.MODE_PRIVATE
        )
    }
    val managerMusic by lazy { MusicManager(activity) }

    fun initialize() {
        NavigationManager.handleNavigationBarVisibility(activity)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
    }
}
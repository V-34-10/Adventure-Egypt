package com.asiaegypt.adventu.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.ui.settings.MusicManager
import com.asiaegypt.adventu.ui.settings.MusicStart

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
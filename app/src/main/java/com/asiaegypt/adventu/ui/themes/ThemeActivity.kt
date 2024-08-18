package com.asiaegypt.adventu.ui.themes

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityThemeBinding
import com.asiaegypt.adventu.ui.levels.LevelActivity
import com.asiaegypt.adventu.ui.settings.MusicManager
import com.asiaegypt.adventu.ui.settings.MusicStart

class ThemeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityThemeBinding.inflate(layoutInflater) }
    private lateinit var preferences: SharedPreferences
    private lateinit var managerMusic: MusicManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeComponents()
        setupThemeButtons()
    }

    private fun initializeComponents() {
        NavigationManager.handleNavigationBarVisibility(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        managerMusic = MusicManager(this)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
    }

    private fun setupThemeButtons() {
        val themeButtons = listOf(
            binding.buttonThemeFirst to R.string.button_theme_first,
            binding.buttonThemeSecond to R.string.button_theme_second,
            binding.buttonThemeThree to R.string.button_theme_three
        )
        themeButtons.forEach { (button, themeResId) ->
            button.setOnClickListener { view ->
                onThemeButtonClicked(view, AnimationUtils.loadAnimation(this, R.anim.scale_animation), themeResId)
            }
        }
    }

    private fun onThemeButtonClicked(view: View, animation: Animation, themeResId: Int) {
        view.startAnimation(animation)
        preferences.edit().putString("themeFindPair", getString(themeResId)).apply()
        startActivity(Intent(this, LevelActivity::class.java))
        managerMusic.release()
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        managerMusic.release()
    }

    override fun onResume() {
        super.onResume()
        managerMusic.resume()
        if (!managerMusic.checkPlaying()) {
            MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
        }
    }

    override fun onPause() {
        super.onPause()
        managerMusic.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        managerMusic.release()
    }
}
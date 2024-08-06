package com.asiaegypt.adventu.ui.themes

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
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
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        managerMusic = MusicManager(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
        choiceThemeGameButton()
    }

    private fun choiceThemeGameButton() {
        var animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)

        binding.buttonThemeFirst.setOnClickListener {
            it.startAnimation(animationButton)
            preferences.edit().putString("themeFindPair", getString(R.string.button_theme_first))
                .apply()
            startActivity(Intent(this@ThemeActivity, LevelActivity::class.java))
            managerMusic.release()
        }
        binding.buttonThemeSecond.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            preferences.edit().putString("themeFindPair", getString(R.string.button_theme_second))
                .apply()
            startActivity(Intent(this@ThemeActivity, LevelActivity::class.java))
            managerMusic.release()
        }
        binding.buttonThemeThree.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            preferences.edit().putString("themeFindPair", getString(R.string.button_theme_three))
                .apply()
            startActivity(Intent(this@ThemeActivity, LevelActivity::class.java))
            managerMusic.release()
        }
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
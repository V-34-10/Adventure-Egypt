package com.asiaegypt.adventu.ui.levels

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityLevelBinding
import com.asiaegypt.adventu.ui.game.SceneActivity
import com.asiaegypt.adventu.ui.settings.MusicManager
import com.asiaegypt.adventu.ui.settings.MusicStart

class LevelActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLevelBinding.inflate(layoutInflater) }
    private lateinit var preferences: SharedPreferences
    private lateinit var managerMusic: MusicManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeComponents()
        setupButtons()
    }

    private fun initializeComponents() {
        NavigationManager.handleNavigationBarVisibility(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        managerMusic = MusicManager(this)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.buttonLevelEasy to R.string.button_level_easy,
            binding.buttonLevelMedium to R.string.button_level_medium,
            binding.buttonLevelHard to R.string.button_level_hard
        )

        val animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)

        buttons.forEach { (button, levelResId) ->
            button.setOnClickListener { view ->
                handleButtonClick(view, levelResId, animation)
            }
        }
    }

    private fun handleButtonClick(view: View, levelResId: Int, animation: Animation) {
        view.startAnimation(animation)
        preferences.edit().putString("levelFindPair", getString(levelResId)).apply()
        startActivity(Intent(this, SceneActivity::class.java))
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
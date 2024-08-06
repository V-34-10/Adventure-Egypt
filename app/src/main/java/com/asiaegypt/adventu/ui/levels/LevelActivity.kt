package com.asiaegypt.adventu.ui.levels

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
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
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        managerMusic = MusicManager(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
        choiceLevelGameButton()
    }

    private fun choiceLevelGameButton() {
        var animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)

        binding.buttonLevelEasy.setOnClickListener {
            it.startAnimation(animationButton)
            preferences.edit().putString("levelFindPair", getString(R.string.button_level_easy))
                .apply()
            startActivity(Intent(this@LevelActivity, SceneActivity::class.java))
            managerMusic.release()
        }
        binding.buttonLevelMedium.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            preferences.edit().putString("levelFindPair", getString(R.string.button_level_medium))
                .apply()
            startActivity(Intent(this@LevelActivity, SceneActivity::class.java))
            managerMusic.release()
        }
        binding.buttonLevelHard.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            preferences.edit().putString("levelFindPair", getString(R.string.button_level_hard))
                .apply()
            startActivity(Intent(this@LevelActivity, SceneActivity::class.java))
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
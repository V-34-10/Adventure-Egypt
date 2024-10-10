package com.asiaegypt.advente.ui.levels

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.advente.R
import com.asiaegypt.advente.databinding.ActivityLevelBinding
import com.asiaegypt.advente.ui.ActivityInitializer
import com.asiaegypt.advente.ui.game.SceneActivity
import com.asiaegypt.advente.ui.settings.MusicStart

class LevelActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLevelBinding.inflate(layoutInflater) }
    private lateinit var initializer: ActivityInitializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer = ActivityInitializer(this)
        initializer.initialize()
        setupButtons()
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
        initializer.preferences.edit().putString("levelFindPair", getString(levelResId)).apply()
        startActivity(Intent(this, SceneActivity::class.java))
        initializer.managerMusic.release()
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        initializer.managerMusic.release()
    }

    override fun onResume() {
        super.onResume()
        initializer.managerMusic.resume()
        if (!initializer.managerMusic.checkPlaying()) {
            MusicStart.musicStartMode(R.raw.music_menu, initializer.managerMusic, initializer.preferences)
        }
    }

    override fun onPause() {
        super.onPause()
        initializer.managerMusic.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        initializer.managerMusic.release()
    }
}
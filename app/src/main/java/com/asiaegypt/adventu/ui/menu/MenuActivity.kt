package com.asiaegypt.adventu.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityMenuBinding
import com.asiaegypt.adventu.ui.ActivityInitializer
import com.asiaegypt.adventu.ui.rules.RulesActivity
import com.asiaegypt.adventu.ui.settings.MusicStart
import com.asiaegypt.adventu.ui.settings.SettingsActivity
import com.asiaegypt.adventu.ui.themes.ThemeActivity

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var initializer: ActivityInitializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer = ActivityInitializer(this)
        initializer.initialize()
        choiceMenuAppButton()
    }

    private fun choiceMenuAppButton() {
        var animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
        binding.buttonStart.setOnClickListener {
            it.startAnimation(animationButton)
            initializer.managerMusic.release()
            startActivity(Intent(this@MenuActivity, ThemeActivity::class.java))
        }
        binding.buttonRules.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            initializer.managerMusic.release()
            startActivity(Intent(this@MenuActivity, RulesActivity::class.java))
        }
        binding.buttonSettings.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            initializer.managerMusic.release()
            startActivity(Intent(this@MenuActivity, SettingsActivity::class.java))
        }
        binding.buttonExit.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            it.postDelayed({
                finishAffinity()
            }, animationButton.duration)
        }
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        initializer.managerMusic.release()
        finishAffinity()
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
package com.asiaegypt.adventu.ui.menu

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityMenuBinding
import com.asiaegypt.adventu.ui.rules.RulesActivity
import com.asiaegypt.adventu.ui.settings.MusicManager
import com.asiaegypt.adventu.ui.settings.MusicStart
import com.asiaegypt.adventu.ui.settings.SettingsActivity
import com.asiaegypt.adventu.ui.themes.ThemeActivity

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var preferences: SharedPreferences
    private lateinit var managerMusic: MusicManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        managerMusic = MusicManager(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
        choiceMenuAppButton()
    }

    private fun choiceMenuAppButton() {
        var animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
        binding.buttonStart.setOnClickListener {
            it.startAnimation(animationButton)
            managerMusic.release()
            startActivity(Intent(this@MenuActivity, ThemeActivity::class.java))
        }
        binding.buttonRules.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            managerMusic.release()
            startActivity(Intent(this@MenuActivity, RulesActivity::class.java))
        }
        binding.buttonSettings.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)
            managerMusic.release()
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
        managerMusic.release()
        finishAffinity()
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
package com.asiaegypt.adventu.ui.rules

import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityRulesBinding
import com.asiaegypt.adventu.ui.settings.MusicManager
import com.asiaegypt.adventu.ui.settings.MusicStart

class RulesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRulesBinding.inflate(layoutInflater) }
    private lateinit var preferences: SharedPreferences
    private lateinit var managerMusic: MusicManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationManager.handleNavigationBarVisibility(this)
        managerMusic = MusicManager(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
        binding.buttonContinue.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation))
            onBackPressed()
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
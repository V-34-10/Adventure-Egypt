package com.asiaegypt.adventu.ui.game

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivitySceneBinding
import com.asiaegypt.adventu.ui.game.fragments.ActecFragment
import com.asiaegypt.adventu.ui.game.fragments.AsianFragment
import com.asiaegypt.adventu.ui.game.fragments.EgyptFragment
import com.asiaegypt.adventu.ui.menu.MenuActivity

class SceneActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySceneBinding.inflate(layoutInflater) }
    private lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationManager.handleNavigationBarVisibility(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        initGameFindPairFragment()
    }

    private fun initGameFindPairFragment() {
        val fragment =
            when (preferences.getString("themeFindPair", getString(R.string.button_theme_first))) {
                getString(R.string.button_theme_second) -> EgyptFragment()
                getString(R.string.button_theme_three) -> AsianFragment()
                else -> ActecFragment()
            }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_game_find_pair, fragment)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            Intent(this@SceneActivity, MenuActivity::class.java).also {
                startActivity(it)
                super.onBackPressed()
                finish()
            }
        }
    }
}
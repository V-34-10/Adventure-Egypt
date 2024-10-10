package com.asiaegypt.advente.ui.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asiaegypt.advente.NetworkManager
import com.asiaegypt.advente.R
import com.asiaegypt.advente.databinding.ActivityMenuBinding
import com.asiaegypt.advente.ui.ActivityInitializer
import com.asiaegypt.advente.ui.ads.AdsSection
import com.asiaegypt.advente.ui.ads.AdsSection.loadAds
import com.asiaegypt.advente.ui.levels.LevelActivity
import com.asiaegypt.advente.ui.offerwall.adapter.GameAdapter
import com.asiaegypt.advente.ui.offerwall.model.Game
import com.asiaegypt.advente.ui.rules.RulesActivity
import com.asiaegypt.advente.ui.settings.MusicStart
import com.asiaegypt.advente.ui.settings.SettingsActivity
import com.asiaegypt.advente.ui.themes.ThemeActivity
import kotlin.system.exitProcess

class MenuActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }
    private lateinit var initializer: ActivityInitializer
    private lateinit var adapter: GameAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer = ActivityInitializer(this)
        initializer.initialize()
        checkOfferWallStatus()
        if (NetworkManager.checkInternet(this)) {
            loadAds(this)
        }
        choiceMenuAppButton()
    }

    private fun checkOfferWallStatus() {
        val offerWallStatus = initializer.preferences.getBoolean("OfferWallStatus", false)
        val games = intent.getParcelableArrayListExtra<Game>("games") ?: emptyList()
        if (offerWallStatus && games.isNotEmpty()) {
            val isWorkingMode = games.any { it.levelTitle.startsWith("https://") }
            if (isWorkingMode) {
                showGamesScreen(games)
            } else {
                showDemoModeScreen(games)
            }
        } else {
            binding.mainMenu.visibility = View.VISIBLE
        }
    }

    private fun showDemoModeScreen(games: List<Game>) {
        for (game in games) {
            initializer.preferences.edit().putString("themeVisualFile${game.moduleCode}", game.visualFile).apply()
        }
        binding.mainMenu.visibility = View.VISIBLE
    }

    private fun showGamesScreen(games: List<Game>) {
        binding.listGames.visibility = View.VISIBLE
        binding.listGames.layoutManager = LinearLayoutManager(this)
        adapter = GameAdapter(this::onGameClick)
        binding.listGames.adapter = adapter
        adapter.updateGames(games)

        Log.d("MenuActivity", "Games received:")
        for (game in games) {
            Log.d(
                "MenuActivity",
                "inx: ${game.moduleCode}, title: ${game.levelTitle}, bgUrl: ${game.visualFile}, fgUrl: ${game.auxImage}, play: ${game.activateText}"
            )
        }
        Log.d("MenuActivity", "Games received: ${games.size}")
        Log.d("MenuActivity", "Intent extras: ${intent.extras}")
    }

    private fun onGameClick(game: Game) {
        if (game.levelTitle.startsWith("https://")) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(game.levelTitle))
            startActivity(intent)
        } else {
            when (game.moduleCode) {
                0 -> initializer.preferences.edit().putString("themeFindPair", getString(R.string.button_theme_first)).apply()
                1 -> initializer.preferences.edit().putString("themeFindPair", getString(R.string.button_theme_second)).apply()
                2 -> initializer.preferences.edit().putString("themeFindPair", getString(R.string.button_theme_three)).apply()
            }
            startActivity(Intent(this@MenuActivity, LevelActivity::class.java))
        }
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
                allFinish()
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
        allFinish()
    }

    override fun onResume() {
        super.onResume()
        initializer.managerMusic.resume()
        if (!initializer.managerMusic.checkPlaying()) {
            MusicStart.musicStartMode(
                R.raw.music_menu,
                initializer.managerMusic,
                initializer.preferences
            )
        }
    }

    override fun onPause() {
        super.onPause()
        initializer.managerMusic.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        AdsSection.interstitialShowed = false
        initializer.managerMusic.release()
    }

    private fun allFinish() {
        exitProcess(0)
    }
}
package com.asiaegypt.adventu.ui.score

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityHighScoreBinding
import com.asiaegypt.adventu.ui.game.ManagerFindPair
import com.asiaegypt.adventu.ui.score.ScoreManager.loadStatsScoreFindPairGame
import com.asiaegypt.adventu.ui.settings.MusicManager
import com.asiaegypt.adventu.ui.settings.MusicStart

class HighScoreActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHighScoreBinding.inflate(layoutInflater) }
    private lateinit var preferences: SharedPreferences
    private lateinit var managerMusic: MusicManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        managerMusic = MusicManager(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
        initScoreFindPairGame()
    }

    @SuppressLint("SetTextI18n")
    private fun initScoreFindPairGame() {
        loadStatsScoreFindPairGame(preferences)

        val easyStatsScoreFindPair = ManagerFindPair.stats["Easy"]!!
        val mediumStatsScoreFindPair = ManagerFindPair.stats["Medium"]!!
        val hardStatsScoreFindPair = ManagerFindPair.stats["Hard"]!!

        binding.titleTime.text =
            getString(R.string.title_time) + easyStatsScoreFindPair.bestTime.toString()
        binding.titleSteps.text =
            getString(R.string.title_steps) + easyStatsScoreFindPair.bestSteps.toString()

        binding.titleTimeMedium.text =
            getString(R.string.title_time_medium) + mediumStatsScoreFindPair.bestTime.toString()
        binding.titleStepsMedium.text =
            getString(R.string.title_steps_medium) + mediumStatsScoreFindPair.bestSteps.toString()

        binding.titleTimeHard.text =
            getString(R.string.title_time_hard) + hardStatsScoreFindPair.bestTime.toString()
        binding.titleStepsHard.text =
            getString(R.string.title_steps_hard) + hardStatsScoreFindPair.bestSteps.toString()
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

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
package com.asiaegypt.adventu.ui.score

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityHighScoreBinding
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
        NavigationManager.handleNavigationBarVisibility(this)
        managerMusic = MusicManager(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        MusicStart.musicStartMode(R.raw.music_menu, managerMusic, preferences)
        initScoreFindPairGame()
    }

    @SuppressLint("SetTextI18n")
    private fun initScoreFindPairGame() {
        loadStatsScoreFindPairGame(preferences)

        val stats = ScoreManager.stats

        val easyStats = stats["Easy"]!!
        val mediumStats = stats["Medium"]!!
        val hardStats = stats["Hard"]!!

        setScoreText(binding.titleTime, R.string.title_time, easyStats.bestTime)
        binding.titleSteps.text = getString(R.string.title_steps) + easyStats.bestSteps

        setScoreText(binding.titleTimeMedium, R.string.title_time_medium, mediumStats.bestTime)
        binding.titleStepsMedium.text = getString(R.string.title_steps_medium) + mediumStats.bestSteps

        setScoreText(binding.titleTimeHard, R.string.title_time_hard, hardStats.bestTime)
        binding.titleStepsHard.text = getString(R.string.title_steps_hard) + hardStats.bestSteps
    }

    @SuppressLint("SetTextI18n")
    private fun setScoreText(textView: TextView, stringResId: Int, bestTime: Long) {
        val formattedTime = if (bestTime == Long.MAX_VALUE) {
            "00:00"
        } else {
            formatTime(bestTime)
        }
        textView.text = getString(stringResId) + formattedTime
    }

    private fun formatTime(timeInMillis: Long): String {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
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
        managerMusic.release()
    }
}
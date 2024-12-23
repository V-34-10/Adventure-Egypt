package com.asiaegypt.advente.ui.score

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.advente.R
import com.asiaegypt.advente.databinding.ActivityHighScoreBinding
import com.asiaegypt.advente.ui.ActivityInitializer
import com.asiaegypt.advente.ui.game.SceneActivity
import com.asiaegypt.advente.ui.score.ScoreManager.loadStatsScoreFindPairGame

class HighScoreActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHighScoreBinding.inflate(layoutInflater) }
    private lateinit var initializer: ActivityInitializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer = ActivityInitializer(this)
        initializer.initialize()
        initScoreFindPairGame()
    }

    @SuppressLint("SetTextI18n")
    private fun initScoreFindPairGame() {
        loadStatsScoreFindPairGame(initializer.preferences)

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

    @SuppressLint("DefaultLocale")
    private fun formatTime(timeInMillis: Long): String {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onResume() {
        super.onResume()
        initializer.managerMusic.resume()
    }

    override fun onPause() {
        super.onPause()
        initializer.managerMusic.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        initializer.managerMusic.release()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        initializer.managerMusic.release()
        startActivity(Intent(this, SceneActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
        finish()
    }
}
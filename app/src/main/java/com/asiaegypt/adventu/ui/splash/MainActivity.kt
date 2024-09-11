package com.asiaegypt.adventu.ui.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.databinding.ActivityMainBinding
import com.asiaegypt.adventu.ui.ads.AdsSection.loadAds
import com.asiaegypt.adventu.ui.menu.MenuActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationManager.handleNavigationBarVisibility(this)

        CoroutineScope(Dispatchers.Main).launch {
            loadingApp()
        }
    }

    private suspend fun loadingApp() {
        withContext(Dispatchers.IO) {
            try {
                loadAds(this@MainActivity)
            } catch (e: Exception) {
                e.printStackTrace()
                startToMenu()
            }
        }
    }

    private fun animationLoading(duration: Long) {
        val displayMetrics = resources.displayMetrics
        val progressBarWidth = (480 * displayMetrics.density).toInt()
        val maxAnimationWidth = progressBarWidth - 10

        val layoutParams = binding.progressBar.line.layoutParams

        val animation = ValueAnimator.ofInt(10, maxAnimationWidth).apply {
            this.duration = duration
            addUpdateListener {
                layoutParams.width = it.animatedValue as Int
                binding.progressBar.line.layoutParams = layoutParams
            }
        }

        animation.start()
    }

    fun startToMenu() {
        animationLoading(3000L)
        startActivity(Intent(this@MainActivity, MenuActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
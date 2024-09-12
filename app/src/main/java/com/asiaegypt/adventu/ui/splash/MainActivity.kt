package com.asiaegypt.adventu.ui.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.NetworkManager
import com.asiaegypt.adventu.databinding.ActivityMainBinding
import com.asiaegypt.adventu.ui.ads.AdsSection.loadAds
import com.asiaegypt.adventu.ui.menu.MenuActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationManager.handleNavigationBarVisibility(this)

        if (NetworkManager.checkInternet(this)) {
            loadAds(this)
        } else {
            startToMenuWithAnimation()
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

    fun startToMenuWithAnimation() {
        animationLoading(3000L)
        lifecycleScope.launch {
            delay(3000L)
            startToMenu()
        }
    }

    fun startToMenu() {
        startActivity(Intent(this@MainActivity, MenuActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
package com.asiaegypt.adventu.ui.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.asiaegypt.adventu.NavigationManager
import com.asiaegypt.adventu.NetworkManager
import com.asiaegypt.adventu.databinding.ActivityMainBinding
import com.asiaegypt.adventu.ui.ads.AdsSection
import com.asiaegypt.adventu.ui.ads.AdsService
import com.asiaegypt.adventu.ui.menu.MenuActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adsService: AdsService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationManager.handleNavigationBarVisibility(this)
        adsService = AdsSection(this, binding)
        loadingApp()
    }

    private fun loadingApp() {
        val animationDuration = 3000L
        animationLoading(animationDuration)
        val isInternetAvailable = NetworkManager.checkInternet(this)
        lifecycleScope.launch {
            delay(animationDuration)
            if (isInternetAvailable) {
                loadAds()
            } else {
                startActivity(Intent(this@MainActivity, MenuActivity::class.java))
                finish()
            }
        }
    }

    private fun animationLoading(duration: Long) {
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
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

    private fun loadAds() {
        val preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        if (preferences.contains("cookies") && preferences.contains("userAgent")) {
            val action = preferences.getString("actionUrl", "")
            val source = preferences.getString("sourceUrl", "")
            val cookies = preferences.getString("cookies", "")
            val userAgent = preferences.getString("userAgent", "")
            adsService.loadImageAds(source.toString(), action.toString(), cookies, userAgent)

        } else {
            adsService.fetchAndLoadAds()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        adsService.cancel()
    }
}
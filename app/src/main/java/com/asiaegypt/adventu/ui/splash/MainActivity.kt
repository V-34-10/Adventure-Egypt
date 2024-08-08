package com.asiaegypt.adventu.ui.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
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
        windowManager.defaultDisplay.getMetrics(DisplayMetrics())
        val screenWidth = resources.displayMetrics.widthPixels - 1000
        val layoutParams = binding.progressBar.line.layoutParams

        val animation = ValueAnimator.ofInt(10, screenWidth).apply {
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
        val actionUrl = preferences.getString("actionUrl", "")
        val sourceUrl = preferences.getString("sourceUrl", "")
        val cookies = preferences.getString("cookies", "")
        val userAgent = preferences.getString("userAgent", "")

        if (actionUrl.isNullOrEmpty() || sourceUrl.isNullOrEmpty()) {
            adsService.fetchAndLoadAds()
        } else {
            adsService.loadImageAds(sourceUrl, actionUrl, cookies, userAgent)
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
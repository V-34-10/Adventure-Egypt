package com.asiaegypt.advente.ui.splash

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.asiaegypt.advente.NavigationManager
import com.asiaegypt.advente.NetworkManager
import com.asiaegypt.advente.databinding.ActivityMainBinding
import com.asiaegypt.advente.ui.ActivityInitializer
import com.asiaegypt.advente.ui.menu.MenuActivity
import com.asiaegypt.advente.ui.offerwall.LoadingOfferWall
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var initializer: ActivityInitializer
    private var loadingOfferWall = LoadingOfferWall()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        NavigationManager.handleNavigationBarVisibility(this)
        initializer = ActivityInitializer(this)
        if (NetworkManager.checkInternet(this)) {
            animationLoading(15000L)
            lifecycleScope.launch {
                delay(3000L)
                loadingOfferWall()
            }
        } else {
            startToMenuWithAnimation()
        }
    }

    private fun loadingOfferWall() {
        loadingOfferWall.fetchInterstitialData { result ->
            when (result) {
                is LoadingOfferWall.Result.Success -> {
                    initializer.preferences.edit().putBoolean("OfferWallStatus", true).apply()
                    startActivity(
                        Intent(
                            this@MainActivity,
                            MenuActivity::class.java
                        ).putParcelableArrayListExtra("games", ArrayList(result.data))
                    )
                    finish()
                }

                is LoadingOfferWall.Result.Failure -> handleFailure(result.exception)
            }
        }
    }

    private fun handleFailure(exception: Exception) {
        initializer.preferences.edit().putBoolean("OfferWallStatus", false).apply()
        lifecycleScope.launch {
            delay(5000L)
            startToMenu()
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

    private fun startToMenuWithAnimation() {
        animationLoading(3000L)
        lifecycleScope.launch {
            delay(3000L)
            startToMenu()
        }
    }

    private fun startToMenu() {
        startActivity(Intent(this@MainActivity, MenuActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingOfferWall.cancel()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
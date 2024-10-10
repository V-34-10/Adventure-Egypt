package com.asiaegypt.advente.ui.rules

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.advente.R
import com.asiaegypt.advente.databinding.ActivityRulesBinding
import com.asiaegypt.advente.ui.ActivityInitializer

class RulesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRulesBinding.inflate(layoutInflater) }
    private lateinit var initializer: ActivityInitializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer = ActivityInitializer(this)
        initializer.initialize()
        binding.buttonContinue.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_animation))
            onBackPressed()
        }
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        initializer.managerMusic.release()
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
}
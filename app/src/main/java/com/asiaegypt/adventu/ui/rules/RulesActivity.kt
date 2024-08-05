package com.asiaegypt.adventu.ui.rules

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityRulesBinding

class RulesActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRulesBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

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
    }
}
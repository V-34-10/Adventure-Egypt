package com.asiaegypt.adventu.ui.themes

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivityThemeBinding
import com.asiaegypt.adventu.ui.ActivityInitializer
import com.asiaegypt.adventu.ui.levels.LevelActivity
import com.asiaegypt.adventu.ui.settings.MusicStart
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class ThemeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityThemeBinding.inflate(layoutInflater) }
    private lateinit var initializer: ActivityInitializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer = ActivityInitializer(this)
        initializer.initialize()
        setupThemeButtons()
        loadImagesFromSharedPreferences()
    }

    private fun loadImagesFromSharedPreferences() {
        val imageButtons = listOf(
            binding.buttonThemeFirst,
            binding.buttonThemeSecond,
            binding.buttonThemeThree
        )
        for (i in imageButtons.indices) {
            val imageUrl = initializer.preferences.getString("themeVisualFile$i", null)
            Log.d("MenuActivity", "Games received: themeVisualFile$i $imageUrl")
            imageButtons[i].loadImage(imageUrl)
        }
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url ?: getDefaultBackground(this))
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    this@loadImage.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun getDefaultBackground(imageView: ImageView): Int {
        return when (imageView.id) {
            R.id.button_theme_first -> R.drawable.button_theme_first
            R.id.button_theme_second -> R.drawable.button_theme_second
            R.id.button_theme_three -> R.drawable.button_theme_three
            else -> R.drawable.button_theme_first
        }
    }

    private fun setupThemeButtons() {
        val themeButtons = listOf(
            binding.buttonThemeFirst to R.string.button_theme_first,
            binding.buttonThemeSecond to R.string.button_theme_second,
            binding.buttonThemeThree to R.string.button_theme_three
        )
        themeButtons.forEach { (button, themeResId) ->
            button.setOnClickListener { view ->
                onThemeButtonClicked(
                    view,
                    AnimationUtils.loadAnimation(this, R.anim.scale_animation),
                    themeResId
                )
            }
        }
    }

    private fun onThemeButtonClicked(view: View, animation: Animation, themeResId: Int) {
        view.startAnimation(animation)
        initializer.preferences.edit().putString("themeFindPair", getString(themeResId)).apply()
        startActivity(Intent(this, LevelActivity::class.java))
        initializer.managerMusic.release()
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
        if (!initializer.managerMusic.checkPlaying()) {
            MusicStart.musicStartMode(
                R.raw.music_menu,
                initializer.managerMusic,
                initializer.preferences
            )
        }
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
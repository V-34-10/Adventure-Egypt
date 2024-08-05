package com.asiaegypt.adventu.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val managerAudioService by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private var defaultVolumeMusic: Int = 50
    private var statusMusic: Boolean = false
    private lateinit var preferences: SharedPreferences
    private lateinit var managerMusic: MusicManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        managerMusic = MusicManager(this)
        preferences = getSharedPreferences("AsianEgyptAdventurePref", MODE_PRIVATE)
        initResourceMusicButtonCheck()
        changeControlVolumeBar()
        choiceSettingsAppButton()
    }

    private fun choiceSettingsAppButton() {
        var animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
        statusMusic = preferences.getBoolean("music_status", false)
        binding.buttonOn.setOnClickListener {
            it.startAnimation(animationButton)

            managerAudioService.setStreamVolume(AudioManager.STREAM_MUSIC, defaultVolumeMusic, 0)

            preferences.edit().putBoolean("music_status", true).apply()

            binding.buttonOn.setBackgroundResource(R.drawable.button_music_on_check)
            binding.buttonOff.setBackgroundResource(R.drawable.button_music_off)

            musicStartMode()
        }
        binding.buttonOff.setOnClickListener {
            animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
            it.startAnimation(animationButton)

            defaultVolumeMusic = managerAudioService.getStreamVolume(AudioManager.STREAM_MUSIC)
            managerAudioService.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)

            preferences.edit().putBoolean("music_status", false).apply()

            binding.buttonOn.setBackgroundResource(R.drawable.button_music_on)
            binding.buttonOff.setBackgroundResource(R.drawable.button_music_off_check)

            musicStartMode()
        }
        binding.buttonContinue.setOnClickListener {
            it.startAnimation(animationButton)
            onBackPressed()
        }
    }

    private fun initResourceMusicButtonCheck() {
        if (preferences.getBoolean("music_status", false)) {
            binding.buttonOn.setBackgroundResource(R.drawable.button_music_on)
            binding.buttonOff.setBackgroundResource(R.drawable.button_music_off_check)
        } else {
            binding.buttonOn.setBackgroundResource(R.drawable.button_music_on_check)
            binding.buttonOff.setBackgroundResource(R.drawable.button_music_off)
        }
    }

    private fun changeControlVolumeBar() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.seekBarVolume.max = maxVolume

        val currentMusicVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        binding.seekBarVolume.progress = currentMusicVolume

        binding.seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progressChanged = false
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    progressChanged = true
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                progressChanged = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (progressChanged) {
                    val volume = seekBar.progress
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
                }
            }
        })
    }

    private fun musicStartMode() {
        statusMusic = preferences.getBoolean("music_status", false)
        if (statusMusic) { managerMusic.apply { play(R.raw.music_menu, true) } }
    }

    override fun onDestroy() {
        super.onDestroy()
        managerMusic.release()
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
    }
}
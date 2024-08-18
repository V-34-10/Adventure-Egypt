package com.asiaegypt.adventu.ui.settings

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.asiaegypt.adventu.R
import com.asiaegypt.adventu.databinding.ActivitySettingsBinding
import com.asiaegypt.adventu.ui.ActivityInitializer

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val managerAudioService by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private var defaultVolumeMusic: Int = 50
    private lateinit var initializer: ActivityInitializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializer = ActivityInitializer(this)
        initializer.initialize()

        initMusicSettings()
        initVolumeControl()
        setupButtons()
    }

    private fun initMusicSettings() {
        val isMusicOn = initializer.preferences.getBoolean("music_status", false)
        updateMusicButtonState(isMusicOn)
    }

    private fun initVolumeControl() {
        val maxVolume = managerAudioService.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        binding.seekBarVolume.max = maxVolume
        binding.seekBarVolume.progress = managerAudioService.getStreamVolume(AudioManager.STREAM_MUSIC)

        binding.seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    managerAudioService.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun setupButtons() {
        val animationButton = AnimationUtils.loadAnimation(this, R.anim.scale_animation)

        binding.buttonOn.setOnClickListener {
            it.startAnimation(animationButton)
            updateMusicButtonState(true)
            managerAudioService.setStreamVolume(AudioManager.STREAM_MUSIC, defaultVolumeMusic, 0)
            initializer.preferences.edit().putBoolean("music_status", true).apply()
            MusicStart.musicStartMode(R.raw.music_menu, initializer.managerMusic, initializer.preferences)
        }

        binding.buttonOff.setOnClickListener {
            it.startAnimation(animationButton)
            updateMusicButtonState(false)
            defaultVolumeMusic = managerAudioService.getStreamVolume(AudioManager.STREAM_MUSIC)
            managerAudioService.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
            initializer.preferences.edit().putBoolean("music_status", false).apply()
            MusicStart.musicStartMode(R.raw.music_menu, initializer.managerMusic, initializer.preferences)
        }

        binding.buttonContinue.setOnClickListener {
            it.startAnimation(animationButton)
            onBackPressed()
        }
    }

    private fun updateMusicButtonState(isMusicOn: Boolean) {
        binding.buttonOn.setBackgroundResource(if (isMusicOn) R.drawable.button_music_on_check else R.drawable.button_music_on)
        binding.buttonOff.setBackgroundResource(if (isMusicOn) R.drawable.button_music_off else R.drawable.button_music_off_check)
    }

    override fun onDestroy() {
        super.onDestroy()
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

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        initializer.managerMusic.release()
    }
}
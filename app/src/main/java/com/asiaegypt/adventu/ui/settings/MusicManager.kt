package com.asiaegypt.adventu.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.annotation.RawRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MusicManager(private val context: Context) {
    private val mediaPlayers = mutableMapOf<Int, MediaPlayer>()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun play(@RawRes soundResId: Int, loop: Boolean = false) {
        if (mediaPlayers.containsKey(soundResId) && mediaPlayers[soundResId]?.isPlaying == true) {
            return
        }

        val mediaPlayer = MediaPlayer.create(context, soundResId).apply {
            isLooping = loop
            setOnCompletionListener {
                if (!loop) {
                    scope.launch {
                        releaseMediaPlayer(soundResId)
                    }
                }
            }
        }

        mediaPlayers[soundResId] = mediaPlayer
        mediaPlayer.start()
    }

    fun stop(@RawRes soundResId: Int) {
        scope.launch {
            releaseMediaPlayer(soundResId)
        }
    }

    fun pause() {
        mediaPlayers.values.forEach {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun resume() {
        mediaPlayers.values.forEach {
            if (!it.isPlaying) {
                it.start()
            }
        }
    }

    fun release() {
        scope.cancel()
        mediaPlayers.values.forEach { it.release() }
        mediaPlayers.clear()
    }

    private fun releaseMediaPlayer(soundResId: Int) {
        mediaPlayers[soundResId]?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayers.remove(soundResId)
        }
    }
}

object MusicStart {
    fun musicStartMode(
        sourceMusic: Int,
        managerMusic: MusicManager,
        preferences: SharedPreferences
    ) {
        val statusMusic = preferences.getBoolean("music_status", false)
        if (statusMusic) {
            managerMusic.apply { play(sourceMusic, true) }
        }
    }
}
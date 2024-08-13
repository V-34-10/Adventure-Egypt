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
    private val media = mutableMapOf<Int, MediaPlayer>()
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun play(@RawRes soundResId: Int, loop: Boolean = false) {
        if (media.containsKey(soundResId) && media[soundResId]?.isPlaying == true) {
            return
        }

        val mediaPlayer = MediaPlayer.create(context, soundResId).apply {
            isLooping = loop
            setOnCompletionListener { if (!loop) scope.launch { releaseMedia(soundResId) } }
            start()
        }

        media[soundResId] = mediaPlayer
    }

    fun pause() = media.values.forEach { if (it.isPlaying) it.pause() }

    fun resume() = media.values.forEach { if (!it.isPlaying) it.start() }

    fun checkPlaying(): Boolean = media.values.any { it.isPlaying }

    fun release() {
        scope.cancel()
        media.values.forEach(MediaPlayer::release)
        media.clear()
    }

    private fun releaseMedia(soundResId: Int) {
        media[soundResId]?.run {
            if (isPlaying) stop()
            release()
            media.remove(soundResId)
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
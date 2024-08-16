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
        val mediaPlayer = media[soundResId] ?: createMediaPlayer(soundResId, loop)
        if (!mediaPlayer.isPlaying) mediaPlayer.start()
    }

    fun pause() = media.values.forEach(MediaPlayer::pauseIfPlaying)

    fun resume() = media.values.forEach(MediaPlayer::startIfNotPlaying)

    fun checkPlaying(): Boolean = media.values.any { it.isPlaying }

    fun release() {
        scope.cancel()
        media.values.forEach(MediaPlayer::release)
        media.clear()
    }

    private fun createMediaPlayer(@RawRes soundResId: Int, loop: Boolean): MediaPlayer {
        return MediaPlayer.create(context, soundResId).apply {
            isLooping = loop
            setOnCompletionListener { if (!loop) scope.launch { releaseMedia(soundResId) } }
            media[soundResId] = this
        }
    }

    private fun releaseMedia(soundResId: Int) {
        media.remove(soundResId)?.releaseIfPlaying()
    }
}

private fun MediaPlayer.pauseIfPlaying() {
    if (isPlaying) pause()
}

private fun MediaPlayer.startIfNotPlaying() {
    if (!isPlaying) start()
}

private fun MediaPlayer.releaseIfPlaying() {
    if (isPlaying) stop()
    release()
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
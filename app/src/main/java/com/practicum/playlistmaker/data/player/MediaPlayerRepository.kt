package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer

interface MediaPlayerRepository(private val mediaPlayer : MediaPlayer) {
    fun preparePlayer(trackPreview: String?, onPrepared: () -> Unit, onError: () -> Unit, onComplete: () -> Unit)
    fun starPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun updateTimerTask()
    fun startTimer()
}
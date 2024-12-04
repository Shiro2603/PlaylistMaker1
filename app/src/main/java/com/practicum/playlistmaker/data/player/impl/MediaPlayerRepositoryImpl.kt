package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private var mediaPlayer : MediaPlayer) : MediaPlayerRepository  {

    private var playerState = STATE_DEFAULT

    override fun preparePlayer(trackPreview: String?) {
        mediaPlayer.setDataSource(trackPreview)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }

    }

    override fun starPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
       mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState = STATE_DEFAULT
    }

    override fun setOnCompletionListener(onCompletion: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    override fun getCurrentPosition() : Int {
       return mediaPlayer.currentPosition
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    }




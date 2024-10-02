package com.practicum.playlistmaker.data.player.impl


import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.MediaPlayerRepository
import com.practicum.playlistmaker.domain.player.PlayerStateCallback
import java.io.IOException

class MediaPlayerRepositoryImpl : MediaPlayerRepository  {

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var playerStateCallback: PlayerStateCallback? = null

    fun setPlayerStateCallback(callback: PlayerStateCallback) {
        playerStateCallback = callback
    }


    override fun preparePlayer(trackPreview: String?) {
        if (trackPreview.isNullOrEmpty()) {
            return
        }

        try {
            mediaPlayer.setDataSource(trackPreview)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
                playerStateCallback?.onPlayerStateChanged(playerState)

            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
                playerStateCallback?.onPlayerStateChanged(playerState)

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun starPlayer() {
        if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
            mediaPlayer.start()
            playerState = STATE_PLAYING
            playerStateCallback?.onPlayerStateChanged(playerState)
        }
    }

    override fun pausePlayer() {
        if (playerState == STATE_PLAYING) {
            mediaPlayer.pause()
            playerState = STATE_PAUSED
            playerStateCallback?.onPlayerStateChanged(playerState)
        }
    }

    override fun getCurrentPosition() {
        mediaPlayer.currentPosition
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

    }

    }




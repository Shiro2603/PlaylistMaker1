package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.MediaPlayerRepository
import java.io.IOException

class MediaPlayerRepositoryImpl(private var mediaPlayer : MediaPlayer) : MediaPlayerRepository  {


    private var playerState = STATE_DEFAULT

    override fun preparePlayer(trackPreview: String?) {
        if (trackPreview.isNullOrEmpty()) {
            return
        }

        try {
            if (playerState != STATE_DEFAULT) {
                mediaPlayer.reset()
            }
            mediaPlayer.setDataSource(trackPreview)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun starPlayer() {
        if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
            mediaPlayer.start()
            playerState = STATE_PLAYING

        }
    }

    override fun pausePlayer() {
        if (playerState == STATE_PLAYING) {
            mediaPlayer.pause()
            playerState = STATE_PAUSED

        }
    }

    override fun release() {
        mediaPlayer.release()
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




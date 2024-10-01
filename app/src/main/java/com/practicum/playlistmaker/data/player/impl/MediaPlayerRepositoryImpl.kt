package com.practicum.playlistmaker.data.player.impl


import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.MediaPlayerRepository
import com.practicum.playlistmaker.ui.media.activity.MediaActivity
import com.practicum.playlistmaker.ui.media.activity.MediaActivity.Companion

class MediaPlayerRepositoryImpl(private val mediaPlayer : MediaPlayer) : MediaPlayerRepository  {

    private var playerState = STATE_DEFAULT


    override fun preparePlayer(trackPreview: String?,
                               onPrepared: () -> Unit,
                               onError: () -> Unit,
                               onComplete: () -> Unit) {
        if (trackPreview.isNullOrEmpty()) {
            onError()
            return
        }

        try {
            mediaPlayer.setDataSource(trackPreview)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED
                onPrepared()
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
                onComplete()
            }
        } catch (e: IOException) {
            onError()
        }
    }
    }

    override fun starPlayer() {
        TODO("Not yet implemented")
    }

    override fun pausePlayer() {
        TODO("Not yet implemented")
    }

    override fun playbackControl() {
        TODO("Not yet implemented")
    }

    override fun updateTimerTask() {
        TODO("Not yet implemented")
    }

    override fun startTimer() {
        TODO("Not yet implemented")
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }
}
package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlistmaker.data.player.MediaPlayerRepository

class MediaPlayerRepositoryImpl(private var mediaPlayer : MediaPlayer) : MediaPlayerRepository  {

    private var playerState = STATE_DEFAULT

    override fun preparePlayer(trackPreview: String?) {

            if (trackPreview.isNullOrEmpty()) {
                throw IllegalArgumentException("Track preview URL cannot be null or empty")
            }

            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(trackPreview)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    playerState = STATE_PREPARED
                    Log.d("MediaPlayerRepository", "Player prepared successfully")
                }
                mediaPlayer.setOnCompletionListener {
                    playerState = STATE_PREPARED
                    Log.d("MediaPlayerRepository", "Playback completed")
                }
            } catch (e: Exception) {
                Log.e("MediaPlayerRepository", "Error preparing player: ${e.message}", e)
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
        mediaPlayer.reset()
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




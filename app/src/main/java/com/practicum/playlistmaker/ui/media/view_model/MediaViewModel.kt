package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import android.os.Handler
import java.text.SimpleDateFormat
import java.util.Locale


class MediaViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val _playerState = MutableLiveData<Int>()
    val playerState: LiveData<Int> = _playerState

    private val _trackTime = MutableLiveData<String>()
    val trackTime: LiveData<String> = _trackTime

    private val handler = Handler()

    private var timerRunnable: Runnable? = null

    fun preparePlayer(trackPreview: String?) {
        mediaPlayerInteractor.preparePlayer(trackPreview)
        _playerState.value = STATE_PREPARED
    }

    private fun startPlayer() {
        mediaPlayerInteractor.starPlayer()
        _playerState.value = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        _playerState.value = STATE_PAUSED
        stopTimer()
    }

    fun playbackControl() {
        when (_playerState.value) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        timerRunnable = object : Runnable {
            override fun run() {
                if (_playerState.value == STATE_PLAYING) {
                    val currentPosition = mediaPlayerInteractor.getCurrentPosition()
                    val minutes = (currentPosition / 1000) / 60
                    val seconds = (currentPosition / 1000) % 60

                    val formattedTime = String.format("%02d:%02d", minutes, seconds)
                    _trackTime.value = formattedTime

                    handler.postDelayed(this, DELAY)
                }
            }
        }
        handler.post(timerRunnable!!)
    }

    private fun stopTimer() {
        timerRunnable?.let { handler.removeCallbacks(it) }
        timerRunnable = null
    }



    companion object{

        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val DELAY = 1000L




        fun getViewModelFactory(
            mediaPlayerInteractor: MediaPlayerInteractor
        ) : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MediaViewModel(mediaPlayerInteractor) as T
                }
            }
    }



}


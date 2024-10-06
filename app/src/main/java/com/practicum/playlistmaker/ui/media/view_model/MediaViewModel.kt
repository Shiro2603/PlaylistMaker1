package com.practicum.playlistmaker.ui.media.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MediaViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {


    private val _playerState = MutableLiveData<Int>()
    val playerState: LiveData<Int> = _playerState

    private val _trackTime = MutableLiveData<String>()
    val trackTime: LiveData<String> = _trackTime


    private var handler: Handler? = Handler(Looper.getMainLooper())



    fun preparePlayer(trackPreview: String?) {
        mediaPlayerInteractor.preparePlayer(trackPreview)
        mediaPlayerInteractor.setOnCompletionListener {
            _playerState.value = STATE_PREPARED
            _trackTime.value = "00:00"
        }
        _playerState.value = STATE_PREPARED
    }

     private fun startPlayer() {
        mediaPlayerInteractor.starPlayer()
        _playerState.value = STATE_PLAYING
         updateTimerTask()

    }

     fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        _playerState.value = STATE_PAUSED

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


    private fun updateTimerTask() {
        val runnable = object : Runnable {
            override fun run() {
                if (_playerState.value == STATE_PLAYING) {
                    val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(mediaPlayerInteractor.getCurrentPosition().toLong()))
                    _trackTime.value = formattedTime
                    handler?.postDelayed(this, DELAY)
                }
            }
        }
        handler?.post(runnable)
    }


    companion object{
        const val DELAY = 1000L
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3

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


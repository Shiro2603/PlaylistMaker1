package com.practicum.playlistmaker.ui.media.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.media.MediaPlayerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MediaViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val saveTrackInteractor: SaveTrackInteractor
) : ViewModel() {

    private val _mediaPlayerState = MutableLiveData<MediaPlayerState>()
    val mediaPlayerState: LiveData<MediaPlayerState> = _mediaPlayerState

    private var handler: Handler? = Handler(Looper.getMainLooper())

    fun preparePlayer(trackPreview: String?) {
        mediaPlayerInteractor.preparePlayer(trackPreview)
        mediaPlayerInteractor.setOnCompletionListener {
            _mediaPlayerState.value = MediaPlayerState.Prepared()
        }
        _mediaPlayerState.value = MediaPlayerState.Prepared()
    }

    private fun startPlayer() {
        mediaPlayerInteractor.starPlayer()
        _mediaPlayerState.value = MediaPlayerState.Playing(getFormattedTrackTime())
        updateTimerTask()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        _mediaPlayerState.value = MediaPlayerState.Paused(getFormattedTrackTime())
    }

    fun playbackControl() {
        when (_mediaPlayerState.value) {
            is MediaPlayerState.Playing -> pausePlayer()
            is MediaPlayerState.Prepared, is MediaPlayerState.Paused -> startPlayer()

            else -> {}
        }
    }

    private fun updateTimerTask() {
        val runnable = object : Runnable {
            override fun run() {
                if (_mediaPlayerState.value is MediaPlayerState.Playing) {
                    _mediaPlayerState.value = MediaPlayerState.Playing(getFormattedTrackTime())
                    handler?.postDelayed(this, DELAY)
                }
            }
        }
        handler?.post(runnable)
    }

    private fun getFormattedTrackTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(mediaPlayerInteractor.getCurrentPosition().toLong()))
    }

    fun getTrack() : Track? {
        return saveTrackInteractor.getTrack()
    }

    companion object{
        const val DELAY = 1000L
    }


}


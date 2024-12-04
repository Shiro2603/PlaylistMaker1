package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.media.MediaPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MediaViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val saveTrackInteractor: SaveTrackInteractor
) : ViewModel() {

    private var timeJob: Job? = null

    private val _mediaPlayerState = MutableLiveData<MediaPlayerState>()
    val mediaPlayerState: LiveData<MediaPlayerState> = _mediaPlayerState

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
        startTimer()

    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        _mediaPlayerState.value = MediaPlayerState.Paused(getFormattedTrackTime())
        timeJob?.cancel()
    }

    fun playbackControl() {
        when (_mediaPlayerState.value) {
            is MediaPlayerState.Playing -> pausePlayer()
            is MediaPlayerState.Prepared, is MediaPlayerState.Paused -> startPlayer()

            else -> {}
        }
    }

    private fun startTimer() {
        timeJob = viewModelScope.launch {
            while (_mediaPlayerState.value is MediaPlayerState.Playing) {
                delay(DELAY)
                _mediaPlayerState.postValue(MediaPlayerState.Playing(getFormattedTrackTime()))
            }
        }
    }

    private fun getFormattedTrackTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(mediaPlayerInteractor.getCurrentPosition().toLong()))
    }

    fun getTrack() : Track? {
        return saveTrackInteractor.getTrack()
    }

    companion object{
        const val DELAY = 300L
    }

}


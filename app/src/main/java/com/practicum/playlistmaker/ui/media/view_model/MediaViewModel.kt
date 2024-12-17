package com.practicum.playlistmaker.ui.media.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.FavoriteTrackInteractor
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
    private val saveTrackInteractor: SaveTrackInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
) : ViewModel() {

    private var timeJob: Job? = null

    private val _mediaPlayerState = MutableLiveData<MediaPlayerState>(MediaPlayerState.Default())
    val mediaPlayerState: LiveData<MediaPlayerState> = _mediaPlayerState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite


    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
        _mediaPlayerState.value = MediaPlayerState.Default()
    }

    fun preparePlayer(trackPreview: String?) {
        mediaPlayerInteractor.preparePlayer(trackPreview)
        _mediaPlayerState.value = MediaPlayerState.Prepared()
        mediaPlayerInteractor.setOnCompletionListener{
            _mediaPlayerState.value = MediaPlayerState.Prepared()
            timeJob?.cancel()
        }
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
        timeJob?.cancel()
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

     fun onFavoriteClicked(track: Track) {
         viewModelScope.launch {
             Log.d("MediaViewModel", "Favorite clicked for track: ${track.trackName}, isFavorite: ${track.isFavorite} and ${_isFavorite.value}")
             if(track.isFavorite) {
                 Log.d("MediaViewModel", "Deleting track from favorites")
                favoriteTrackInteractor.deleteTrack(track)
                 track.isFavorite = false
                 _isFavorite.postValue(false)
             } else {
                 Log.d("MediaViewModel", "Adding track to favorites")
                 favoriteTrackInteractor.addTrack(track)
                 track.isFavorite = true
                 _isFavorite.postValue(true)
             }
         }
     }

    fun checkFavorite(trackId: Int) {
        viewModelScope.launch {
            val favorite = favoriteTrackInteractor.isTrackFavorite(trackId)
            _isFavorite.postValue(favorite)
        }
    }

    companion object{
        const val DELAY = 300L
    }

}


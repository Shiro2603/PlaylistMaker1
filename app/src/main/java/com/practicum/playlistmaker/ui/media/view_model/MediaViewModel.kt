package com.practicum.playlistmaker.ui.media.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.mediateka.PlayListInteractor
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.player.FavoriteTrackInteractor
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.media.MediaPlayerState
import com.practicum.playlistmaker.ui.mediateka.PlayListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MediaViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    private var timeJob: Job? = null

    private val _mediaPlayerState = MutableLiveData<MediaPlayerState>(MediaPlayerState.Default())
    val mediaPlayerState: LiveData<MediaPlayerState> = _mediaPlayerState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _stateLiveData = MutableLiveData<PlayListState>()
    val stateLiveData : LiveData<PlayListState> = _stateLiveData

    private val _stateAddTrack = MutableLiveData<Boolean?>(null)
    val stateAddTrack: LiveData<Boolean?> = _stateAddTrack

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


     fun onFavoriteClicked(track: Track) {
         viewModelScope.launch {
             Log.d("MediaViewModel", "Favorite clicked for track: ${track.trackName}, isFavorite: ${track.isFavorite} and ${_isFavorite.value}")
             if(_isFavorite.value == true) {
                 Log.d("MediaViewModel", "Deleting track from favorites")
                favoriteTrackInteractor.deleteTrack(track)
                 _isFavorite.postValue(false)
             } else {
                 Log.d("MediaViewModel", "Adding track to favorites")
                 favoriteTrackInteractor.addTrack(track)
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

    fun getPlayList() {
        viewModelScope.launch {
            playListInteractor
                .getPlayList()
                .collect {
                    _stateLiveData.value = if (it.isEmpty()) {
                        PlayListState.Empty
                    } else {
                        PlayListState.Content(it)
                    }
                }
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: PlayList) {
        if(!playlist.tracksIds.contains(track.trackId)) {
            viewModelScope.launch {
                playListInteractor.addTrackToPlayList(track, playlist)
                _stateAddTrack.postValue(true)
            }
        } else {
            _stateAddTrack.postValue(false)
        }
    }


    companion object{
        const val DELAY = 300L
    }

}


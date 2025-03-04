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
import com.practicum.playlistmaker.ui.media.fragment.AudioPlayerControl
import kotlinx.coroutines.launch


class MediaViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val playListInteractor: PlayListInteractor,
) : ViewModel() {

    private val _mediaPlayerState = MutableLiveData<MediaPlayerState>(MediaPlayerState.Default())
    val mediaPlayerState: LiveData<MediaPlayerState> = _mediaPlayerState

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _stateLiveData = MutableLiveData<PlayListState>()
    val stateLiveData : LiveData<PlayListState> = _stateLiveData

    private val _stateAddTrack = MutableLiveData<Boolean?>(null)
    val stateAddTrack: LiveData<Boolean?> = _stateAddTrack

    private var audioPlayerControl: AudioPlayerControl? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                _mediaPlayerState.postValue(it)
            }
        }
    }

    fun onPlayerButtonClicked() {
        if (_mediaPlayerState.value is MediaPlayerState.Playing) {
            audioPlayerControl?.pausePlayer()
        } else {
            audioPlayerControl?.startPlayer()
        }
    }


    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
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

}


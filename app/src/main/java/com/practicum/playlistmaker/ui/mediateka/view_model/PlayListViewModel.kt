package com.practicum.playlistmaker.ui.mediateka.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.mediateka.PlayListInteractor
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.ui.mediateka.PlayListState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayListViewModel(private val playListInteractor: PlayListInteractor,
                        private val sharingInteractor: SharingInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlayListState>()
    val stateLiveData : LiveData<PlayListState> = _stateLiveData

    private val _stateTrackLiveData = MutableLiveData<List<Track>>()
    val stateTrackLiveData : LiveData<List<Track>> = _stateTrackLiveData

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

     fun getPlayListById(playListId: Int) {
        viewModelScope.launch {
            val playlist = playListInteractor.getPlayListById(playListId)
            _stateLiveData.value = PlayListState.SinglePlaylist(playlist)
        }
    }

    fun loadPlayListTrack(trackIds: List<Int?>) {
        Log.d("PlayListViewModel", "Track IDs: $trackIds")
        viewModelScope.launch {
            playListInteractor
                .getTrackForPlayList(trackIds)
                .collect { trackList ->
                    _stateTrackLiveData.postValue(trackList)
                }
        }
    }

     fun calculateTotalDuration(tracks: List<Track>): String {
        val totalDurationMillis = tracks.sumOf { it.trackTimeMillis }
        return SimpleDateFormat("mm", Locale.getDefault()).format(totalDurationMillis)
    }

     fun deleteTrack(trackId : Int, playList: PlayList) {
        viewModelScope.launch {
            playListInteractor.deletePlayListTrack(trackId, playList)
            val updatedPlayList = playListInteractor.getPlayListById(playList.id!!)
            _stateLiveData.postValue(PlayListState.SinglePlaylist(updatedPlayList))
            loadPlayListTrack(playList.tracksIds)
        }

    }

    fun deletePlayList(playList: PlayList) {
        viewModelScope.launch {
            playListInteractor.deletePlayList(playList)
        }
    }

    fun sharingPlayList(context: Context, playList: PlayList, track: List<Track>) {
        sharingInteractor.sharePlaylist(context, playList, track)
    }

}
package com.practicum.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.player.FavoriteTrackInteractor
import com.practicum.playlistmaker.ui.mediateka.FavoriteState
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor
) : ViewModel() {

    private val _stateLiveDate = MutableLiveData<FavoriteState>()
    val stateLiveDate : LiveData<FavoriteState> = _stateLiveDate

     fun getFavoriteTrack()    {
           viewModelScope.launch {
               favoriteTrackInteractor
                   .getAllTracks()
                   .collect {
                       _stateLiveDate.value = if(it.isEmpty()) {
                           FavoriteState.Empty
                       } else {
                           FavoriteState.Content(it)
                       }
                   }
           }
    }

}
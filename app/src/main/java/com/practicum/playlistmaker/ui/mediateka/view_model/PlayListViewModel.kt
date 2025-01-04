package com.practicum.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.mediateka.PlayListInteractor
import com.practicum.playlistmaker.ui.mediateka.PlayListState
import kotlinx.coroutines.launch

class PlayListViewModel(private val playListInteractor: PlayListInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlayListState>()
    val stateLiveData : LiveData<PlayListState> = _stateLiveData

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

}
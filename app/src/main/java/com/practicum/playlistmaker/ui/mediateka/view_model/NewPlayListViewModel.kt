package com.practicum.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.mediateka.PlayListInteractor
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import kotlinx.coroutines.launch

class NewPlayListViewModel(private val playListInteractor: PlayListInteractor) : ViewModel() {

    fun createPlayList(playList: PlayList) {
        viewModelScope.launch {
            playListInteractor.createPlayList(playList)
        }
    }


}
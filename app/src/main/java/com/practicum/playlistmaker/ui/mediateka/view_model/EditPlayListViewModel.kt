package com.practicum.playlistmaker.ui.mediateka.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.mediateka.PlayListInteractor
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import kotlinx.coroutines.launch

class EditPlayListViewModel(private val playListInteractor: PlayListInteractor) : NewPlayListViewModel(
    playListInteractor
) {

    private val _playlistData = MutableLiveData<PlayList>()
    val playlistData: LiveData<PlayList> get() = _playlistData

    fun loadPlaylist(playlistId: Int) {
        viewModelScope.launch {
            val playlist = playListInteractor.getPlayListById(playlistId)
            _playlistData.postValue(playlist)
        }
    }

    override fun savePlayList(name: String, description: String, imageUri: String?) {
        viewModelScope.launch {
            val playlist = _playlistData.value?.copy(
                playListName = name,
                playListDescription = description,
                urlImager = imageUri
            ) ?: PlayList(
                id = null,
                playListName = name,
                playListDescription = description,
                urlImager = imageUri,
                tracksCount = 0,
                tracksIds = listOf(),

            )
            playListInteractor.createPlayList(playlist)
        }
    }
}


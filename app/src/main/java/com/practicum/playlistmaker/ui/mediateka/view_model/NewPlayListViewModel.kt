package com.practicum.playlistmaker.ui.mediateka.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.mediateka.PlayListInteractor
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import kotlinx.coroutines.launch

class NewPlayListViewModel(private val playListInteractor: PlayListInteractor) : ViewModel() {

    private fun createPlayList(playList: PlayList) {
        viewModelScope.launch {
            playListInteractor.createPlayList(playList)
        }
    }

    fun savePlayList(
        name: String,
        description: String,
        imageUri: String?,

    ) {
        val playList = PlayList(
            id = null,
            playListName = name,
            playListDescription = description,
            urlImager = imageUri,
            tracksCount = 0,
            tracksIds = mutableListOf(),
        )

        createPlayList(playList)

    }

    fun saveToStorage(uri: Uri) : String {
        return playListInteractor.saveToStorage(uri)
    }

    fun getTrackToStorage(image: String) : Uri {
        return playListInteractor.getImageToStorage(image)
    }

}
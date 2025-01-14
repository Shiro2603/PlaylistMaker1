package com.practicum.playlistmaker.domain.mediateka.impl

import android.net.Uri
import com.practicum.playlistmaker.data.mediateka.PlayListRepository
import com.practicum.playlistmaker.data.storage.StorageRepository
import com.practicum.playlistmaker.domain.mediateka.PlayListInteractor
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    private val repository: PlayListRepository,
    private val imageStorage: StorageRepository) : PlayListInteractor {

    override suspend fun createPlayList(playList: PlayList) {
        repository.createPlayList(playList)
    }

    override suspend fun getPlayList(): Flow<List<PlayList>> {
        return repository.getPlayList()
    }

    override suspend fun addTrackToPlayList(track: Track, playList: PlayList)  {
        repository.addToPlayList(track, playList)
    }

    override fun saveToStorage(uri: Uri) : String {
        return imageStorage.saveToStorage(uri)
    }

    override fun getImageToStorage(image: String): Uri {
        return imageStorage.getImageToStorage(image)
    }


}
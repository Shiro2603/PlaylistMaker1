package com.practicum.playlistmaker.domain.mediateka

import android.net.Uri
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun createPlayList(playList: PlayList)
    suspend fun getPlayList() : Flow<List<PlayList>>
    suspend fun addTrackToPlayList(track: Track, playList: PlayList)
    fun saveToStorage(uri: Uri) : String
    fun getImageToStorage(image: String) : Uri
}
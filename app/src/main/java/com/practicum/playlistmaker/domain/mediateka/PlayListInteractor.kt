package com.practicum.playlistmaker.domain.mediateka

import android.net.Uri
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun createPlayList(playList: PlayList)
    suspend fun getPlayList() : Flow<List<PlayList>>
    suspend fun addTrackToPlayList(track: Track, playList: PlayList)
    suspend fun getPlayListById(playListId: Int) : PlayList
    suspend fun getTrackForPlayList(trackIds: List<Int?>) : Flow<List<Track>>
    suspend fun deletePlayListTrack(trackId: Int, playList: PlayList)
    suspend fun deletePlayList(playList: PlayList)
    fun saveToStorage(uri: Uri) : String
    fun getImageToStorage(image: String) : Uri
}
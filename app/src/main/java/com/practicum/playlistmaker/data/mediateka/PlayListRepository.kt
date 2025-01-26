package com.practicum.playlistmaker.data.mediateka

import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    suspend fun createPlayList(playList: PlayList)
    fun getPlayList() : Flow<List<PlayList>>
    suspend fun addToPlayList(track: Track, playList: PlayList)
    suspend fun getPlayListById(playListId: Int) : PlayList
    fun getTrackForPlayList(trackIds: List<Int?>) : Flow<List<Track>>
    suspend fun deletePlayListTrack(trackId : Int, playList: PlayList)
    suspend fun deletePlayList(playList: PlayList)

}
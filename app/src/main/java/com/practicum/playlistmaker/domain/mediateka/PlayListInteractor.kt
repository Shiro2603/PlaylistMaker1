package com.practicum.playlistmaker.domain.mediateka

import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun createPlayList(playList: PlayList)
    suspend fun getPlayList() : Flow<List<PlayList>>
    suspend fun addTrackToPlayList(track: Track, playList: PlayList)
}
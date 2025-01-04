package com.practicum.playlistmaker.domain.mediateka

import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    suspend fun createPlayList(playList: PlayList)
    suspend fun getPlayList() : Flow<List<PlayList>>

}
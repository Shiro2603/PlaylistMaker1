package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {

    fun searchTrack(expression: String): Flow<Pair<List<Track>?, String?>>

}
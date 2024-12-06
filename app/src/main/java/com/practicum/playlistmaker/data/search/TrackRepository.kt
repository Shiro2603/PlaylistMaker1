package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(expression: String): Flow<Resource<List<Track>>>
}
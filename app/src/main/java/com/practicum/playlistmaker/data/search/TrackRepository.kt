package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}
package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.model.Track

interface SaveTrackRepository {
    fun saveTrack(track: Track)
    fun getTrack(): Track?
}
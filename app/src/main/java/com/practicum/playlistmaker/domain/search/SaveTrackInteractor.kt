package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.Track

interface SaveTrackInteractor {
    fun saveTrack(track : Track)
    fun getTrack(): Track?
}
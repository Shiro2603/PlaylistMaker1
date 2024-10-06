package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.Track

interface SearchHistoryInteractor {

    fun saveSearchHistory(history: List<Track>)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()
    fun addTrackToHistory(track: Track)

}
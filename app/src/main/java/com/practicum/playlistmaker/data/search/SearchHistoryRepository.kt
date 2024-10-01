package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.search.model.Track

interface SearchHistoryRepository {

    fun addTrackToHistory(track: Track)
    fun getSearchHistory(): List<Track>
    fun clearHistory()
    fun saveSearchHistory(history: List<Track>)
}
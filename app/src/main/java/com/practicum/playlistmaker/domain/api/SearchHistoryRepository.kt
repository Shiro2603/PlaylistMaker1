package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryRepository {

    fun addTrackToHistory(track: Track)

    fun getSearchHistory(): List<Track>

    fun clearHistory()

    fun saveSearchHistory(history: List<Track>)
}
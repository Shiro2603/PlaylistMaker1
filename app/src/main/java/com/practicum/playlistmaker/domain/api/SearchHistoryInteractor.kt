package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {

    fun saveSearchHistory(history: List<Track>)
    fun getSearchHistory(): List<Track>
    fun clearSearchHistory()

}
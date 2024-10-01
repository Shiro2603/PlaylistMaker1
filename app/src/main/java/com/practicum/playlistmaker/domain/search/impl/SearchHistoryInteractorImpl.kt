package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.data.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.search.model.Track

class SearchHistoryInteractorImpl(private val repository : SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun saveSearchHistory(history: List<Track>) {
        repository.saveSearchHistory(history)
    }

    override fun getSearchHistory(): List<Track> {
        return repository.getSearchHistory()
    }

    override fun clearSearchHistory() {
        repository.clearHistory()
    }
}
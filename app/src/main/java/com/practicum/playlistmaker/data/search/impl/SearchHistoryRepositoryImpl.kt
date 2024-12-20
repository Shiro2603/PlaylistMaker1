package com.practicum.playlistmaker.data.search.impl

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.search.model.Track

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson : Gson
) : SearchHistoryRepository {

    companion object {
        private const val PREFS_NAME = "search_history_prefs"
        private const val KEY_SEARCH_HISTORY = "search_history"
    }

    private val maxHistorySize = 10

    override fun addTrackToHistory(track: Track) {
        val history = getSearchHistory().toMutableList()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)

        if (history.size > maxHistorySize) {
            history.removeAt(history.size - 1)
        }
        
        saveSearchHistory(history)
    }

    override fun getSearchHistory(): List<Track> {
        val json = sharedPreferences.getString(KEY_SEARCH_HISTORY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(KEY_SEARCH_HISTORY).apply()
    }

    override fun saveSearchHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPreferences.edit().putString(KEY_SEARCH_HISTORY, json).apply()
    }
}

package com.practicum.playlistmaker.ui.search

import com.practicum.playlistmaker.domain.search.model.Track

sealed class SearchScreenState {

    object Loading : SearchScreenState()

    data class Content(
        val tracks: List<Track>,
    ) : SearchScreenState()

    object NotFound : SearchScreenState()

    data class Error(
        val errorMessage: String
    ) : SearchScreenState()

    data class History(
        val historyTracks: List<Track>
    ) : SearchScreenState()
}

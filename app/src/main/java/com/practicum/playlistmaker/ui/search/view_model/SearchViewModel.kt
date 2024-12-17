package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.search.SearchScreenState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val saveTrackInteractor: SaveTrackInteractor
) : ViewModel() {

    private val _screenStateLiveData = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenStateLiveData

    init {
        loadSearchHistory()
    }

    fun search(query: String) {
        if (query.isNotEmpty()) {

            _screenStateLiveData.postValue(SearchScreenState.Loading)

            viewModelScope.launch {
                trackInteractor
                    .searchTrack(query)
                    .collect{ pair ->
                        processResult(pair.first, pair.second)
                    }
            }

        }
    }

    private fun processResult(track: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (track != null) {
            tracks.addAll(track)
        }

        when{
            errorMessage != null -> {
                _screenStateLiveData.postValue(SearchScreenState.Error(errorMessage))
            }

            tracks.isEmpty() -> {
                _screenStateLiveData.postValue(SearchScreenState.NotFound)
            }

            else -> {
                _screenStateLiveData.postValue(SearchScreenState.Content(tracks))
            }
        }

    }

    fun loadSearchHistory() {
        val historyTracks = searchHistoryInteractor.getSearchHistory()
        _screenStateLiveData.postValue(SearchScreenState.History(historyTracks))
    }

    fun clearHistory() {
        searchHistoryInteractor.clearSearchHistory()
        loadSearchHistory()
    }

    fun saveTrack (tracks : Track) {
        saveTrackInteractor.saveTrack(tracks)
    }

    fun getSearchHistory() : List<Track> {
        return searchHistoryInteractor.getSearchHistory()
    }

    fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addTrackToHistory(track)
        loadSearchHistory()
    }
}








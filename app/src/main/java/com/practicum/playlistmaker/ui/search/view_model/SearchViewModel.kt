package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.search.SearchScreenState

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
            _screenStateLiveData.value = SearchScreenState.Loading

            trackInteractor.searchTrack(query, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    _screenStateLiveData.postValue(SearchScreenState.Loading)

                    if (errorMessage != null) {
                        _screenStateLiveData.postValue(SearchScreenState.Error(errorMessage))
                    } else if (foundTracks != null) {
                        if (foundTracks.isEmpty()) {
                            _screenStateLiveData.postValue(SearchScreenState.NotFound)
                        } else {
                            _screenStateLiveData.postValue(SearchScreenState.Content(foundTracks))
                        }
                    }
                }
            })
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








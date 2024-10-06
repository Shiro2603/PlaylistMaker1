package com.practicum.playlistmaker.ui.search.view_model
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.domain.search.SearchHistoryInteractor
import com.practicum.playlistmaker.domain.search.TracksInteractor
import com.practicum.playlistmaker.domain.search.model.Track




class SearchViewModel(
    private val trackInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _tracksLiveData = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracksLiveData

    private val _historyLivaData = MutableLiveData<List<Track>>()
    val history: LiveData<List<Track>> = _historyLivaData

    val errorMessageLiveData = MutableLiveData<String?>()
    val isLoading = MutableLiveData<Boolean>()
    val isNotFoundVisible = MutableLiveData<Boolean>()


    fun search(query: String) {
        if (query.isNotEmpty()) {
            isLoading.value = true
            isNotFoundVisible.value = false

            trackInteractor.searchTrack(query, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    isLoading.postValue(false)
                    if (errorMessage != null) {
                        errorMessageLiveData.postValue(errorMessage)
                    } else if (foundTracks != null) {
                        if (foundTracks.isEmpty()) {
                            isNotFoundVisible.postValue(true)
                        } else {
                            _tracksLiveData.postValue(foundTracks)
                        }
                    }
                }
            })
        }
    }



    fun updateHistoryAdapter() {
        _historyLivaData.value = searchHistoryInteractor.getSearchHistory()
    }

    fun clearHistory () {
        searchHistoryInteractor.clearSearchHistory()
        updateHistoryAdapter()
    }


    companion object{
        fun getViewModelFactoryForSearch(
            trackInteractor: TracksInteractor,
            searchHistoryInteractor: SearchHistoryInteractor
        ) : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(trackInteractor, searchHistoryInteractor) as T
                }
            }
    }


}







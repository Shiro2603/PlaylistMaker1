package com.practicum.playlistmaker.ui.mediateka

import com.practicum.playlistmaker.domain.search.model.Track

sealed class FavoriteState {

    class Content(val track: List<Track>) : FavoriteState()

    object Empty : FavoriteState()

}
package com.practicum.playlistmaker.ui.mediateka

import com.practicum.playlistmaker.domain.mediateka.model.PlayList

sealed class PlayListState {

    class Content(val playList: List<PlayList>) : PlayListState()

    object Empty : PlayListState()

}
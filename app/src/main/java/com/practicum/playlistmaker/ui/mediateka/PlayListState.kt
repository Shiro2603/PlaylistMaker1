package com.practicum.playlistmaker.ui.mediateka

import com.practicum.playlistmaker.domain.mediateka.model.PlayList

sealed class PlayListState {

    class Content(val playList: List<PlayList>) : PlayListState()
    class SinglePlaylist(val playList: PlayList) : PlayListState()

    object Empty : PlayListState()

}
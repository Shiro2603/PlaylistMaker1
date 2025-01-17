package com.practicum.playlistmaker.domain.sharing

import android.content.Context
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlaylist(context: Context, playList: PlayList, trackList: List<Track>)
}
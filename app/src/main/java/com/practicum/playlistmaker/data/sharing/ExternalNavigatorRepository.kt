package com.practicum.playlistmaker.data.sharing

import android.content.Context
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigatorRepository {

    fun shareLink(shareLink: String)
    fun openEmail(emailData: EmailData)
    fun OpenLink(termsLink: String)
    fun sharePlaylist(context: Context, playList: PlayList, trackList: List<Track>)
}
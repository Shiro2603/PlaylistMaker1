package com.practicum.playlistmaker.data.sharing

import com.practicum.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigatorRepository {

    fun shareLink(shareLink: String)
    fun openEmail(emailData: EmailData)
    fun OpenLink(termsLink: String)
}
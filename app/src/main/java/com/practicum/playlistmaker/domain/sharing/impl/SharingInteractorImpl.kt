package com.practicum.playlistmaker.domain.sharing.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.practicum.playlistmaker.data.sharing.ResourceProvider
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigatorRepository,
    private val resourceProvider : ResourceProvider
) : SharingInteractor {

    override fun shareApp() {
        val shareLink = getShareAppLink()
        externalNavigator.shareLink(shareLink)
    }

    override fun openTerms() {
        val termsLink = getTermsLink()
        externalNavigator.OpenLink(termsLink)
    }

    override fun openSupport() {
        val emailData = getSupportEmailData()
        externalNavigator.openEmail(emailData)
    }

    override fun sharePlaylist(playList: PlayList, trackList: List<Track>) {
        externalNavigator.sharePlaylist(playList, trackList)
    }

    private fun getShareAppLink(): String {
        return resourceProvider.getString(R.string.shareMessage)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = resourceProvider.getString(R.string.email),
            subject = resourceProvider.getString(R.string.themeMail),
            text = resourceProvider.getString(R.string.messageMail)
        )
    }

    private fun getTermsLink(): String {
        return resourceProvider.getString(R.string.userAgreement)
    }

}
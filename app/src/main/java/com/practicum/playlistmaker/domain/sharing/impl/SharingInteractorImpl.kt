package com.practicum.playlistmaker.domain.sharing.impl


import android.content.Context
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorRepositoryImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigatorRepository,
    private val context: Context
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

    private fun getShareAppLink(): String {
        return context.getString(R.string.shareMessage)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            email = context.getString(R.string.email),
            subject = context.getString(R.string.themeMail),
            text = context.getString(R.string.messageMail)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.userAgreement)
    }

}
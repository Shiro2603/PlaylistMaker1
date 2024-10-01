package com.practicum.playlistmaker.data.sharing.impl

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorRepositoryImpl(private val activity: Activity) : ExternalNavigatorRepository {

    override fun shareLink(shareLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plan"
        shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.shareMessage))
        ContextCompat.startActivity(activity, shareIntent, null)
    }

    override fun openEmail(emailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(activity.getString(R.string.email)))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.themeMail))
        supportIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.messageMail))
        ContextCompat.startActivity(activity, supportIntent, null)
    }

    override fun OpenLink(termsLink: String) {
        val userAgreement = Intent(Intent.ACTION_VIEW)
        userAgreement.setData(Uri.parse(activity.getString(R.string.userAgreement)))
        ContextCompat.startActivity(activity, userAgreement, null)
    }
}
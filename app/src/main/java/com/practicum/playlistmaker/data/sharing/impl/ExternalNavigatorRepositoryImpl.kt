package com.practicum.playlistmaker.data.sharing.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.sharing.ExternalNavigatorRepository
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class ExternalNavigatorRepositoryImpl(private val context: Context) : ExternalNavigatorRepository {

    override fun shareLink(shareLink: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plan"
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.shareMessage))
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(context, shareIntent, null)
    }

    override fun openEmail(emailData: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.email)))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.themeMail))
        supportIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.messageMail))
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(context, supportIntent, null)
    }

    override fun OpenLink(termsLink: String) {
        val userAgreement = Intent(Intent.ACTION_VIEW)
        userAgreement.setData(Uri.parse(context.getString(R.string.userAgreement)))
        userAgreement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(context, userAgreement, null)
    }
}
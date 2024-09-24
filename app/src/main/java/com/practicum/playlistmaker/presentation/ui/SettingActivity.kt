package com.practicum.playlistmaker.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.SharedPreferencesTheme

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        val buttonArrowBack = findViewById<ImageView>(R.id.button_arrow_back)
        buttonArrowBack.setOnClickListener{
            finish()
        }

        val buttonShare = findViewById<TextView>(R.id.button_share)
        val buttonSupport = findViewById<TextView>(R.id.button_support)
        val buttonUserAgreement = findViewById<TextView>(R.id.button_user_agreement)
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switch_theme)


        buttonShare.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plan"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareMessage))
            startActivity(shareIntent)
        }

        buttonSupport.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.themeMail))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.messageMail))
            startActivity(supportIntent)

        }

        buttonUserAgreement.setOnClickListener{
            val userAgreement = Intent(Intent.ACTION_VIEW)
            userAgreement.setData(Uri.parse(getString(R.string.userAgreement)))
            startActivity(userAgreement)
        }

        val sharedPreferencesTheme = applicationContext as SharedPreferencesTheme
        themeSwitcher.isChecked = sharedPreferencesTheme.darkTheme


        themeSwitcher.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as SharedPreferencesTheme).switchTheme(checked)
        }





    }
}
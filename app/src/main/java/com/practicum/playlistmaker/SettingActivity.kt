package com.practicum.playlistmaker

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        buttonShare.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            startActivity(shareIntent)
        }




    }
}
package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.search.SongApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private val retrofit = Retrofit.Builder()
    .baseUrl("https://itunes.apple.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private val songsApiService = retrofit.create<SongApi>()

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonBack = findViewById<ImageView>(R.id.btnArrayBackMedia)
        val countrySong = findViewById<TextView>(R.id.countrySong)
        val genreSong = findViewById<TextView>(R.id.genreSong)
        val yearSong = findViewById<TextView>(R.id.yearSong)
        val albumSong = findViewById<TextView>(R.id.albumSong)
        val durationSong = findViewById<TextView>(R.id.durationSong)
        val trackName = findViewById<TextView>(R.id.trackName)
        val trackGroup = findViewById<TextView>(R.id.trackGroup)
        val trackTime = findViewById<TextView>(R.id.trackTime)


        buttonBack.setOnClickListener {
            finish()
        }






    }
}
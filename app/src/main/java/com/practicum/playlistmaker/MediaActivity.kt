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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Until.dpToPx
import com.practicum.playlistmaker.search.SongApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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


        val trackNameIntent = intent.getStringExtra("TRACK_NAME")
        val trackArtistIntent = intent.getStringExtra("ARTIST_NAME")
        val trackTimeIntent = intent.getStringExtra("TRACK_TIME")
        val trackPictureIntent = intent.getStringExtra("TRACK_PICTURE")
        val trackCollectionIntent = intent.getStringExtra("TRACK_COLLECTION")
        val trackReleaseDateIntent = intent.getStringExtra("TRACK_RELEASE_DATE")
        val trackGenreIntent = intent.getStringExtra("TRACK_GENRE")
        val trackCountryIntent = intent.getStringExtra("TRACK_COUNTRY")

        val buttonBack = findViewById<ImageView>(R.id.btnArrayBackMedia)
        val countrySong = findViewById<TextView>(R.id.countrySong)
        val genreSong = findViewById<TextView>(R.id.genreSong)
        val yearSong = findViewById<TextView>(R.id.yearSong)
        val albumSong = findViewById<TextView>(R.id.albumSong)
        val durationSong = findViewById<TextView>(R.id.durationSong)
        val trackName = findViewById<TextView>(R.id.trackName)
        val trackGroup = findViewById<TextView>(R.id.trackGroup)
        val trackTime = findViewById<TextView>(R.id.trackTime)
        val trackPicture = findViewById<ImageView>(R.id.trackPicture)




        trackName.text = trackNameIntent
        trackGroup.text = trackArtistIntent
        durationSong.text = trackTimeIntent
        Glide.with(trackPicture)
            .load(trackPictureIntent)
            .placeholder(R.drawable.ic_placeholder_media)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(trackPicture)

        albumSong.text = trackCollectionIntent
        yearSong.text = trackReleaseDateIntent
        genreSong.text = trackGenreIntent
        countrySong.text = trackCountryIntent



        buttonBack.setOnClickListener {
            finish()
        }

















    }
}
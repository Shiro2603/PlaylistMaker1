package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
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

        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)


        val savedTrackName = sharedPreferences.getString("TRACK_NAME", null)
        val savedTrackArtist = sharedPreferences.getString("ARTIST_NAME", null)
        val savedTrackTime = sharedPreferences.getString("TRACK_TIME", null)
        val savedTrackPicture = sharedPreferences.getString("TRACK_PICTURE", null)
        val savedTrackCollection = sharedPreferences.getString("TRACK_COLLECTION", null)
        val savedTrackReleaseDate = sharedPreferences.getString("TRACK_RELEASE_DATE", null)
        val savedTrackGenre = sharedPreferences.getString("TRACK_GENRE", null)
        val savedTrackCountry = sharedPreferences.getString("TRACK_COUNTRY", null)


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
        val mediaLayout = findViewById<ScrollView>(R.id.mediaLayout)


        mediaLayout.visibility = if(savedTrackName == null) View.GONE else View.VISIBLE




        trackName.text = savedTrackName
        trackGroup.text = savedTrackArtist
        durationSong.text = savedTrackTime
        Glide.with(trackPicture)
            .load(savedTrackPicture)
            .placeholder(R.drawable.ic_placeholder_media)
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(trackPicture)

        albumSong.text = savedTrackCollection
        yearSong.text = savedTrackReleaseDate
        genreSong.text = savedTrackGenre
        countrySong.text = savedTrackCountry



        buttonBack.setOnClickListener {
            finish()
        }




    }





}
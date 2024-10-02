package com.practicum.playlistmaker.ui.media.activity

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import com.practicum.playlistmaker.ui.media.view_model.MediaViewModel
import com.practicum.playlistmaker.ui.media.view_model.MediaViewModel.Companion.getViewModelFactory
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.util.Until.dpToPx
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale




class MediaActivity : AppCompatActivity() {


    private lateinit var binding : ActivityMediaBinding
    private var handler: Handler? = null
    private val mediaPlayer : MediaPlayerInteractor
        get() = Creator.provideMediaPlayerInteractor()

    private lateinit var viewModel : MediaViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this, getViewModelFactory(mediaPlayer))[MediaViewModel::class.java]

        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        handler = Handler(Looper.getMainLooper())


        val savedTrackName = sharedPreferences.getString("TRACK_NAME", null)
        val savedTrackArtist = sharedPreferences.getString("ARTIST_NAME", null)
        val savedTrackTime = sharedPreferences.getString("TRACK_TIME", null)
        val savedTrackPicture = sharedPreferences.getString("TRACK_PICTURE", null)
        val savedTrackCollection = sharedPreferences.getString("TRACK_COLLECTION", null)
        val savedTrackReleaseDate = sharedPreferences.getString("TRACK_RELEASE_DATE", null)
        val savedTrackGenre = sharedPreferences.getString("TRACK_GENRE", null)
        val savedTrackCountry = sharedPreferences.getString("TRACK_COUNTRY", null)
        val savedTrackPreview = sharedPreferences.getString("TRACK_PREVIEW", null)



        binding.mediaLayout.visibility = if (savedTrackName == null) View.GONE else View.VISIBLE

        binding.btnArrayBackMedia.setOnClickListener {
            finish()
        }



        binding.trackName.text = savedTrackName
        binding.trackGroup.text = savedTrackArtist
        binding.durationSong.text = savedTrackTime
        Glide.with(binding.trackPicture)
            .load(savedTrackPicture)
            .placeholder(R.drawable.ic_placeholder_media)
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(binding.trackPicture)

        binding.albumSong.text = savedTrackCollection
        binding.yearSong.text = savedTrackReleaseDate
        binding.genreSong.text = savedTrackGenre
        binding.countrySong.text = savedTrackCountry


        savedTrackPreview?.let {
            viewModel.preparePlayer(it)
        }

        viewModel.playerState.observe(this) { state ->
            updatePlayPauseButton(state == MediaViewModel.STATE_PLAYING)
        }

        binding.btnPlay.setOnClickListener {
            viewModel.playbackControl()

        }

    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        if (isPlaying) {
            binding.btnPlay.setImageResource(R.drawable.ic_button_stop)
        } else {
            binding.btnPlay.setImageResource(R.drawable.ic_button_play)
        }

    }





}
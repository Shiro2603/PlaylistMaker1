package com.practicum.playlistmaker.ui.media.activity

import android.content.Context
import android.content.SharedPreferences
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
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.media.view_model.MediaViewModel
import com.practicum.playlistmaker.ui.media.view_model.MediaViewModel.Companion.getViewModelFactory
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.util.Until.dpToPx
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




class MediaActivity : AppCompatActivity() {


    private lateinit var binding : ActivityMediaBinding
    private var handler: Handler? = null
    private val mediaPlayer : MediaPlayerInteractor
        get() = Creator.provideMediaPlayerInteractor()
    private lateinit var viewModel : MediaViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var saveTrack: SaveTrackInteractor


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

        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        saveTrack = Creator.provideSaveTrackInteractor(sharedPreferences)

        handler = Handler(Looper.getMainLooper())


        val track = intent.getSerializableExtra(SAVE_TRACK) as? Track
            ?: return

        val savedTrack = saveTrack.getTrack()



        viewModel = ViewModelProvider(this, getViewModelFactory(mediaPlayer, track))[MediaViewModel::class.java]


        binding.btnArrayBackMedia.setOnClickListener {
            finish()
        }


        binding.mediaLayout.visibility = if (track.trackName == null) View.GONE else View.VISIBLE

        binding.btnArrayBackMedia.setOnClickListener { finish() }

        binding.trackName.text = track.trackName
        binding.trackGroup.text = track.artistName
        binding.durationSong.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(track.trackTimeMillis))
        val artworkUrl512 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(binding.trackPicture)
            .load(artworkUrl512)
            .placeholder(R.drawable.ic_placeholder_media)
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(binding.trackPicture)
        binding.albumSong.text = track.collectionName
        binding.yearSong.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)!!)
        binding.genreSong.text = track.primaryGenreName
        binding.countrySong.text = track.country



        viewModel.preparePlayer(track.previewUrl)

        savedTrack?.let {
            binding.trackName.text = it.trackName
            binding.trackGroup.text = it.artistName
            binding.durationSong.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(it.trackTimeMillis))
            val artworkUrl512 = it.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
            Glide.with(binding.trackPicture)
                .load(artworkUrl512)
                .placeholder(R.drawable.ic_placeholder_media)
                .transform(RoundedCorners(dpToPx(8f, this)))
                .into(binding.trackPicture)
            binding.albumSong.text = it.collectionName
            binding.yearSong.text = it.releaseDate
            binding.genreSong.text = it.primaryGenreName
            binding.countrySong.text = it.country

        }



        viewModel.playerState.observe(this) { state ->
            updatePlayPauseButton(state == MediaViewModel.STATE_PLAYING)
            if(state == MediaViewModel.STATE_PREPARED) {
                binding.trackTime.text = "00:00"
                binding.btnPlay.setImageResource(R.drawable.ic_button_play)

            }
        }

        viewModel.trackTime.observe(this) { formattedTime ->
            binding.trackTime.text = formattedTime
        }


        binding.btnPlay.setOnClickListener {
            viewModel.playbackControl()

        }



    }


    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
        handler?.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler?.removeCallbacksAndMessages(null)
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        if (isPlaying) {
            binding.btnPlay.setImageResource(R.drawable.ic_button_stop)
        } else {
            binding.btnPlay.setImageResource(R.drawable.ic_button_play)
        }

    }

    companion object {
        const val SAVE_TRACK = "track"
        const val SAVE_TRACK_TIME = "track_time"
        const val SAVE_IS_PLAYING = "is_playing"
    }

}
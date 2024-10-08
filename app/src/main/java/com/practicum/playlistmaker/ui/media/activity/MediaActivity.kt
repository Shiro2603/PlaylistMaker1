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
import com.practicum.playlistmaker.ui.MediaPlayerState
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
    private val saveTrack: SaveTrackInteractor
        get() = Creator.provideSaveTrackInteractor(this)
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

        handler = Handler(Looper.getMainLooper())


        val track = intent.getSerializableExtra(SAVE_TRACK) as? Track ?: saveTrack.getTrack()





        viewModel = ViewModelProvider(this, getViewModelFactory(mediaPlayer))[MediaViewModel::class.java]


        binding.btnArrayBackMedia.setOnClickListener {
            finish()
        }

        binding.mediaLayout.visibility = if (track?.trackName == null) View.GONE else View.VISIBLE

        if (track != null) {
            binding.trackName.text = track.trackName
            binding.trackGroup.text = track.artistName
            binding.durationSong.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(track.trackTimeMillis))

            val artworkUrl512 = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: "" // Проверка на null
            Glide.with(binding.trackPicture)
                .load(artworkUrl512)
                .placeholder(R.drawable.ic_placeholder_media)
                .transform(RoundedCorners(dpToPx(8f, this)))
                .into(binding.trackPicture)

            binding.albumSong.text = track.collectionName
            if (!track.releaseDate.isNullOrEmpty()) {
                binding.yearSong.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)!!)
            } else {
                binding.yearSong.text = "Unknown"
            }

            binding.genreSong.text = track.primaryGenreName
            binding.countrySong.text = track.country
        }

        binding.btnArrayBackMedia.setOnClickListener { finish() }


        viewModel.preparePlayer(track?.previewUrl)



        viewModel.mediaPlayerState.observe(this) { state ->
            when (state) {
                is MediaPlayerState.Prepared -> {
                    binding.trackTime.text = state.trackTime
                    updatePlayPauseButton(isPlaying = false)
                }
                is MediaPlayerState.Playing -> {
                    binding.trackTime.text = state.trackTime
                    updatePlayPauseButton(isPlaying = true)
                }
                is MediaPlayerState.Paused -> {
                    binding.trackTime.text = state.trackTime
                    updatePlayPauseButton(isPlaying = false)
                }

                else -> {}
            }
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
    }

}
package com.practicum.playlistmaker.ui.media.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.media.MediaPlayerState
import com.practicum.playlistmaker.ui.media.view_model.MediaViewModel
import com.practicum.playlistmaker.util.Until.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MediaActivity : AppCompatActivity() {

    private var _binding: ActivityMediaBinding? = null
    private val binding: ActivityMediaBinding get() = requireNotNull(_binding) {"Binding wasn't initiliazed!" }
    private val viewModel by viewModel<MediaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val track = intent.getSerializableExtra(SAVE_TRACK) as? Track ?: viewModel.getTrack()

        if (track != null) {
            track.trackId?.let { viewModel.checkFavorite(it) }
        }

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

        binding.buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.mediaPlayerState.observe(this) {
            binding.trackTime.text = it.progress
            if(it.isPlaying) {
                binding.buttonPlay.setImageResource(R.drawable.ic_button_stop)
            } else {
                binding.buttonPlay.setImageResource(R.drawable.ic_button_play)
            }
        }

        binding.btnLikeSong.setOnClickListener {
            viewModel.onFavoriteClicked(track!!)
        }

        viewModel.isFavorite.observe(this) {

            if(it == true) {
                binding.btnLikeSong.setImageResource(R.drawable.ic_button_liked)
            } else {
                binding.btnLikeSong.setImageResource(R.drawable.ic_button_not_like)
            }

        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object {
        const val SAVE_TRACK = "track"
    }

}
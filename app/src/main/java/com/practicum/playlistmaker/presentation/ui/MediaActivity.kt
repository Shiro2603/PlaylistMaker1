package com.practicum.playlistmaker.presentation.ui

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Until.dpToPx
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale




class MediaActivity : AppCompatActivity() {

    private var mediaPlayer = MediaPlayer()

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT
    private lateinit var buttonPlay: ImageView
    private lateinit var trackTime: TextView
    private var handler: Handler? = null


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


        val buttonBack = findViewById<ImageView>(R.id.btnArrayBackMedia)
        val countrySong = findViewById<TextView>(R.id.countrySong)
        val genreSong = findViewById<TextView>(R.id.genreSong)
        val yearSong = findViewById<TextView>(R.id.yearSong)
        val albumSong = findViewById<TextView>(R.id.albumSong)
        val durationSong = findViewById<TextView>(R.id.durationSong)
        val trackName = findViewById<TextView>(R.id.trackName)
        val trackGroup = findViewById<TextView>(R.id.trackGroup)
        trackTime = findViewById(R.id.trackTime)
        val trackPicture = findViewById<ImageView>(R.id.trackPicture)
        val mediaLayout = findViewById<ScrollView>(R.id.mediaLayout)
        buttonPlay = findViewById(R.id.btnPlay)


        mediaLayout.visibility = if (savedTrackName == null) View.GONE else View.VISIBLE

        fun preparePlayer() {
            if (savedTrackPreview.isNullOrEmpty()) {
                Toast.makeText(this, "Песня отсутствует", Toast.LENGTH_SHORT).show()
                buttonPlay.isEnabled = false
                return
            }

            try {
                mediaPlayer.setDataSource(savedTrackPreview)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener {
                    buttonPlay.isEnabled = true
                    playerState = STATE_PREPARED
                }
                mediaPlayer.setOnCompletionListener {
                    buttonPlay.setImageResource(R.drawable.ic_button_play)
                    playerState = STATE_PREPARED
                    trackTime.text = getString(R.string.trackTimer)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Не удалось воспроизвести трек", Toast.LENGTH_SHORT).show()
            }
        }

        preparePlayer()




        buttonPlay.setOnClickListener {
            playbackControl()
        }



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

    private fun starPlayer() {
        mediaPlayer.start()
        buttonPlay.setImageResource(R.drawable.ic_button_stop)
        playerState = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        buttonPlay.setImageResource(R.drawable.ic_button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                starPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler?.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler?.removeCallbacksAndMessages(null)
    }

    private fun updateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val remainingTime = mediaPlayer.currentPosition

                if (playerState == STATE_PLAYING) {
                    trackTime.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler?.postDelayed(this, DELAY)
                }

            }
        }
    }

    private fun startTimer() {
        handler?.post(
            updateTimerTask()
        )
    }

}
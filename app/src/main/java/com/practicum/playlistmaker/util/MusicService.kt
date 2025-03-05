package com.practicum.playlistmaker.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.media.MediaPlayerState
import com.practicum.playlistmaker.ui.media.AudioPlayerControl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MusicService : Service(), AudioPlayerControl {

    private companion object {
        const val LOG_TAG = "MusicService"
        const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        const val SERVICE_NOTIFICATION_ID = 1
    }

    private val binder = MusicServiceBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var songUrl = ""
    private var trackName = ""
    private var artistName = ""
    private val _playerState = MutableStateFlow<MediaPlayerState>(MediaPlayerState.Default())
    private var timerJob : Job? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        createNotificationChannel()

    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onBind(intent: Intent?): IBinder {
        songUrl = intent?.getStringExtra("song_url") ?: ""
        trackName = intent?.getStringExtra("track_name") ?: ""
        artistName = intent?.getStringExtra("artist_name") ?: ""
        initMediaPlayer()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)
    }

    inner class MusicServiceBinder : Binder() {
        fun getService() : MusicService = this@MusicService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_STICKY
    }

    private fun initMediaPlayer() {

        if (songUrl.isEmpty()) return

        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(songUrl)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            _playerState.value = MediaPlayerState.Prepared()
        }
        mediaPlayer?.setOnCompletionListener {
            _playerState.value = MediaPlayerState.Prepared()
            stopForeground()
            timerJob?.cancel()
        }

    }

    override fun getPlayerState(): StateFlow<MediaPlayerState> {
        return _playerState.asStateFlow()
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        _playerState.value = MediaPlayerState.Playing(getCurrentPlayerPosition())
        starTimer()

    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        _playerState.value = MediaPlayerState.Paused(getCurrentPlayerPosition())
    }

    override fun startForeground() {
        startForeground(
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            getForegroundServiceTypeConstant())
    }

    override fun stopForeground() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun releasePlayer() {
        timerJob?.cancel()
        mediaPlayer?.stop()
        _playerState.value = MediaPlayerState.Default()
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground()

    }

    private fun starTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (_playerState.value is MediaPlayerState.Playing) {
                delay(300L)
               _playerState.value = MediaPlayerState.Playing(getCurrentPlayerPosition())
            }
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    private fun createServiceNotification() : Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Playlist Maker")
            .setContentText("$artistName - $trackName")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    private fun getForegroundServiceTypeConstant() : Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }
    }



    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(mediaPlayer?.currentPosition?.toLong() ?: 0))
    }

}
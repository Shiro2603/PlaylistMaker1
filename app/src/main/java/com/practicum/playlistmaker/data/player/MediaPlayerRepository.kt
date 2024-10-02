package com.practicum.playlistmaker.data.player

import android.media.MediaPlayer

interface MediaPlayerRepository {
    fun preparePlayer(trackPreview: String?)
    fun starPlayer()
    fun pausePlayer()
    fun getCurrentPosition()
}
package com.practicum.playlistmaker.ui.media

import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState() : StateFlow<MediaPlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun startForeground()
    fun stopForeground()
}
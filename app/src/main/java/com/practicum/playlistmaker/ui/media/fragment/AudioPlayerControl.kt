package com.practicum.playlistmaker.ui.media.fragment

import com.practicum.playlistmaker.ui.media.MediaPlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun getPlayerState() : StateFlow<MediaPlayerState>
    fun startPlayer()
    fun pausePlayer()
}
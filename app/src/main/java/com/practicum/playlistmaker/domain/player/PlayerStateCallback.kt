package com.practicum.playlistmaker.domain.player

interface PlayerStateCallback {
    fun onPlayerStateChanged(state: Int)
}
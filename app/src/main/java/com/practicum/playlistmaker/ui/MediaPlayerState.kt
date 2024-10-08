package com.practicum.playlistmaker.ui

sealed class MediaPlayerState {
    data class Prepared(val trackTime: String = "00:00") : MediaPlayerState()
    data class Playing(val trackTime: String) : MediaPlayerState()
    data class Paused(val trackTime: String) : MediaPlayerState()
}
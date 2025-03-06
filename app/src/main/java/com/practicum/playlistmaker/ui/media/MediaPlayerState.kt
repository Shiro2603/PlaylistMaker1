package com.practicum.playlistmaker.ui.media

sealed class MediaPlayerState(val progress: String, val isPlaying: Boolean) {

    class Default : MediaPlayerState("00:00", false)

    class Prepared : MediaPlayerState( "00:00", false)

    class Playing(progress: String) : MediaPlayerState(progress, true)

    class Paused(progress: String) : MediaPlayerState(progress, false)

}
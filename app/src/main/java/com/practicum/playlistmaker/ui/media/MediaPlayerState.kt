package com.practicum.playlistmaker.ui.media

sealed class MediaPlayerState(val isPlayButtonEnable: Boolean, val progress: String, val isPlaying: Boolean) {

    class Default : MediaPlayerState(false, "00:00", false)

    class Prepared : MediaPlayerState(true, "00:00", false)

    class Playing(progress: String) : MediaPlayerState(true, progress, true)

    class Paused(progress: String) : MediaPlayerState(true, progress, false)

}
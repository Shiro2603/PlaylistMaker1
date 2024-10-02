package com.practicum.playlistmaker.domain.player

interface MediaPlayerInteractor {
    fun preparePlayer(trackPreview: String?)
    fun starPlayer()
    fun pausePlayer()
    fun getCurrentPosition()
}
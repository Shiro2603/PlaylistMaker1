package com.practicum.playlistmaker.domain.player

interface MediaPlayerInteractor {
    fun preparePlayer(trackPreview: String?)
    fun starPlayer()
    fun pausePlayer()
    fun release()
    fun setOnCompletionListener (onCompletion: () -> Unit)
    fun getCurrentPosition() : Int

}
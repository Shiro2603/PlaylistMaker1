package com.practicum.playlistmaker.data.player


interface MediaPlayerRepository {
    fun preparePlayer(trackPreview: String?)
    fun starPlayer()
    fun pausePlayer()
    fun release()
    fun setOnCompletionListener (onCompletion: () -> Unit)
    fun getCurrentPosition() : Int

}
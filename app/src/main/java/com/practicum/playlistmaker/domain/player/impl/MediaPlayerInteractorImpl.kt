package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.data.player.MediaPlayerRepository
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerStateCallback

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository) : MediaPlayerInteractor {

    private var playerStateCallback: PlayerStateCallback? = null

    override fun preparePlayer(trackPreview: String?) {
        repository.preparePlayer(trackPreview)
    }

    override fun starPlayer() {
       repository.starPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun getCurrentPosition() {
        repository.getCurrentPosition()
    }


}
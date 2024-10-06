package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.data.player.MediaPlayerRepository
import com.practicum.playlistmaker.domain.player.MediaPlayerInteractor

class MediaPlayerInteractorImpl(private val repository: MediaPlayerRepository) : MediaPlayerInteractor {


    override fun preparePlayer(trackPreview: String?) {
        repository.preparePlayer(trackPreview)
    }

    override fun starPlayer() {
       repository.starPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

   override fun release () {
       repository.release()
   }

    override fun setOnCompletionListener(onCompletion: () -> Unit) {
        repository.setOnCompletionListener(onCompletion)
    }

    override fun getCurrentPosition() : Int {
       return repository.getCurrentPosition()
    }


}
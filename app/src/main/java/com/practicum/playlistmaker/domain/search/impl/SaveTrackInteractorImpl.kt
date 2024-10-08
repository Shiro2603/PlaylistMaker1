package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.data.search.SaveTrackRepository
import com.practicum.playlistmaker.domain.search.SaveTrackInteractor
import com.practicum.playlistmaker.domain.search.model.Track

class SaveTrackInteractorImpl(private val repository : SaveTrackRepository) : SaveTrackInteractor {
    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun getTrack(): Track? {
        return repository.getTrack()
    }
}
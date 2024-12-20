package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.data.player.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.player.FavoriteTrackInteractor
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(private val repository: FavoriteTracksRepository) : FavoriteTrackInteractor {

    override suspend fun addTrack(track: Track) {
        repository.addToFavorite(track)
    }

    override suspend fun deleteTrack(track: Track) {
        repository.deleteToFavorite(track)
    }

    override suspend fun getAllTracks(): Flow<List<Track>> {
        return repository.getAllTracks()

    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        return repository.isTrackFavorite(trackId)
    }
}
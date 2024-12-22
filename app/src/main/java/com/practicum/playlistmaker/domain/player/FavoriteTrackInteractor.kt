package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {

    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    suspend fun getAllTracks(): Flow<List<Track>>
    suspend fun isTrackFavorite(trackId: Int) : Boolean
}
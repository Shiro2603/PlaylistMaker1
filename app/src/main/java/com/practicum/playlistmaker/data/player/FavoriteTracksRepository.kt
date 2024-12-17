package com.practicum.playlistmaker.data.player

import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {

    suspend fun addToFavorite(track: Track)
    suspend fun deleteToFavorite(track: Track)
    suspend fun getAllTracks(): Flow<List<Track>>
    suspend fun isTrackFavorite(trackId: Int) : Boolean

}
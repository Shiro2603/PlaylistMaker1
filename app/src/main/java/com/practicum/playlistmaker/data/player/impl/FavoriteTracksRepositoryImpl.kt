package com.practicum.playlistmaker.data.player.impl

import com.practicum.playlistmaker.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.data.player.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteTracksRepository {

    override suspend fun addToFavorite(track: Track) {
        val trackEntity = trackDbConvertor.map(track)
        appDatabase.trackDao().addTracks(trackEntity)
    }

    override suspend fun deleteToFavorite(track: Track) {
        track.trackId?.let { appDatabase.trackDao().deleteTracks(it) }
    }

    override fun getAllTracks(): Flow<List<Track>> = flow {
        val trackEntities = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(trackEntities))
    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        return appDatabase.trackDao().getIdTrack().contains(trackId)
    }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>) : List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }


}
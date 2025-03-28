package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.search.model.Track

class TrackDbConvertor {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            id = null,
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
        )
    }

    fun map(track: TrackEntity) : Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis ?:0L,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
        )
    }

}
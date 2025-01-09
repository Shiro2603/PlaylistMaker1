package com.practicum.playlistmaker.data.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.data.db.entity.PlayListTracksEntity
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track

class PlayListConvertor {

   private val gson = Gson()

    fun map(playList: PlayList) : PlayListEntity {
        return PlayListEntity(
            id = null,
            playList.playListName,
            playList.playListDescription,
            playList.urlImager,
            tracksIds = serializeTracksIds(playList.tracksIds),
            playList.tracksCount,
        )
    }

    fun map(playList: PlayListEntity) : PlayList {
        return PlayList(
            id = null,
            playList.playListName,
            playList.playListDescription,
            playList.urlImager,
            tracksIds = deserializeTracksIds(playList.tracksIds),
            playList.tracksCount,
        )
    }

    fun map(track: Track) : PlayListTracksEntity{
        return PlayListTracksEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
        )
    }

    fun map(track: PlayListTracksEntity) : Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis ?: 0L,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = false,
        )
    }

    private fun serializeTracksIds(trackId: MutableList<Int?>) : String {
        return gson.toJson(trackId)
    }

    private fun deserializeTracksIds(trackId: String?) : MutableList<Int?> {
        val type = object : TypeToken<MutableList<Int>>() {}.type
        return gson.fromJson(trackId, type)

    }

}
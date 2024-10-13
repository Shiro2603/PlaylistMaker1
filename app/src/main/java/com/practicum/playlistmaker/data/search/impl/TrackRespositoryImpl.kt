package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.search.TrackRepository
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.util.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    Track(
                        it.trackId ?: 0,
                        it.trackName ?: "Unknown Track",
                        it.artistName ?: "Unknown Artist",
                        it.trackTime ?: 1000000L,
                        it.artworkUrl100 ?: "",
                        it.collectionName ?: "",
                        it.releaseDate,
                        it.primaryGenreName ?: "Unknown Genre",
                        it.country ?: "Unknown Country",
                        it.previewUrl ?: ""
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }

    }

}

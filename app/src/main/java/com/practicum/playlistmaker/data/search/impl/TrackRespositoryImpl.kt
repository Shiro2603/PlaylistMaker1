package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.search.TrackRepository
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

         when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                with(response as TrackSearchResponse) {
                    val data = results.map {
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
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }

    }

}

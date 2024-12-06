package com.practicum.playlistmaker.data.search.impl

import com.practicum.playlistmaker.R
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
                emit(Resource.Error(R.string.checkingTheConnection.toString()))
            }
            200 -> {
                with(response as TrackSearchResponse) {
                    val data = results.mapNotNull {
                        Track(
                            it.trackId ,
                            it.trackName ,
                            it.artistName ,
                            it.trackTime ?: 0L ,
                            it.artworkUrl100 ,
                            it.collectionName ,
                            it.releaseDate ,
                            it.primaryGenreName ,
                            it.country ,
                            it.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error(R.string.serverError.toString()))
            }
        }

    }

}

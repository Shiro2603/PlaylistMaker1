package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongApi {
    @GET("/search?entity=song")

    fun search(@Query("term") expression : String) : Call<TrackSearchResponse>

}
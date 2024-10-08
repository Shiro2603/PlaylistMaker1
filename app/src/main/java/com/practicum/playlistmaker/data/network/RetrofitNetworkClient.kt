package com.practicum.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RetrofitNetworkClient(private val context: Context) : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val songsApiService = retrofit.create(SongApi::class.java)

    override fun doRequest(dto: Any): Response {
        if(!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if(dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        val resp = songsApiService.search(dto.expression).execute()
        val body = resp.body()

        return if (body != null) {
            body.apply { resultCode = resp.code() }
        } else {
            Response().apply { resultCode = resp.code() }
        }
        }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }


}


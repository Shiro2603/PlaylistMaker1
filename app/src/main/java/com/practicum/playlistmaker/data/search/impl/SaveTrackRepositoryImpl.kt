package com.practicum.playlistmaker.data.search.impl

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.search.SaveTrackRepository
import com.practicum.playlistmaker.domain.search.model.Track



class SaveTrackRepositoryImpl(private val context: Context) : SaveTrackRepository {

    private fun sharedPreferences(): SharedPreferences {
        return context.getSharedPreferences("track_prefs", Context.MODE_PRIVATE)
    }


    override fun saveTrack(track: Track) {
        val editor = sharedPreferences().edit()
        val json = Gson().toJson(track)
        editor.putString("SAVE_TRACK", json)
        editor.apply()
    }

    override fun getTrack(): Track? {
        val json = sharedPreferences().getString("SAVE_TRACK", null)
        return if (json != null) {
            Gson().fromJson(json, Track::class.java)
        } else {
            null
        }
    }


}
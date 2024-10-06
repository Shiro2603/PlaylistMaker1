package com.practicum.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.data.search.SaveTrackRepository
import com.practicum.playlistmaker.domain.search.model.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.Context

class SaveTrackRepositoryImpl(private val sharedPreferences: SharedPreferences) : SaveTrackRepository {


    override fun saveTrack(track: Track) {
        val editor = sharedPreferences.edit()
        editor.putInt("TRACK_ID", track.trackId)
        editor.putString("TRACK_NAME", track.trackName)
        editor.putString("ARTIST_NAME", track.artistName)
        editor.putString("TRACK_TIME", SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(track.trackTimeMillis)))
        editor.putString("TRACK_PICTURE", track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
        editor.putString("TRACK_COLLECTION", track.collectionName)
        editor.putString("TRACK_RELEASE_DATE", SimpleDateFormat("yyyy", Locale.getDefault()).format(
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)!!
        ))
        editor.putString("TRACK_GENRE", track.primaryGenreName)
        editor.putString("TRACK_COUNTRY", track.country)
        editor.putString("TRACK_PREVIEW", track.previewUrl)
        editor.apply()
    }

    override fun getTrack(): Track? {
        val savedIdTrack = sharedPreferences.getInt("TRACK_ID", 0) ?: return null
        val savedTrackName = sharedPreferences.getString("TRACK_NAME", null) ?: return null
        val savedTrackArtist = sharedPreferences.getString("ARTIST_NAME", null) ?: return null
        val savedTrackTime = sharedPreferences.getString("TRACK_TIME", null) ?: return null
        val savedTrackPicture = sharedPreferences.getString("TRACK_PICTURE", null) ?: return null
        val savedTrackCollection = sharedPreferences.getString("TRACK_COLLECTION", null) ?: return null
        val savedTrackReleaseDate = sharedPreferences.getString("TRACK_RELEASE_DATE", null) ?: return null
        val savedTrackGenre = sharedPreferences.getString("TRACK_GENRE", null) ?: return null
        val savedTrackCountry = sharedPreferences.getString("TRACK_COUNTRY", null) ?: return null
        val savedTrackPreview = sharedPreferences.getString("TRACK_PREVIEW", null) ?: return null

        return Track(
            trackId = savedIdTrack,
            trackName = savedTrackName,
            artistName = savedTrackArtist,
            trackTimeMillis = SimpleDateFormat("mm:ss", Locale.getDefault()).parse(savedTrackTime)!!.time,
            artworkUrl100 = savedTrackPicture,
            collectionName = savedTrackCollection,
            releaseDate = savedTrackReleaseDate,
            primaryGenreName = savedTrackGenre,
            country = savedTrackCountry,
            previewUrl = savedTrackPreview
        )

    }

}
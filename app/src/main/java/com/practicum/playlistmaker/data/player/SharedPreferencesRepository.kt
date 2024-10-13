package com.practicum.playlistmaker.data.player

interface SharedPreferencesRepository {

    fun saveTrackName(trackName: String)
    fun saveArtistName(artistName: String)
    fun saveTrackTime(trackTime: String)
    fun saveTrackPicture(trackPicture: String)
    fun saveTrackCollection(trackCollection: String)
    fun saveTrackReleaseDate(trackReleaseDate: String)
    fun saveTrackGenre(trackGenre: String)
    fun saveTrackCountry(trackCountry: String)
    fun saveTrackPreview(trackPreview: String)
    fun getTrackName()
    fun getArtistName()
    fun getTrackTime()
    fun getTrackPicture()
    fun getTrackCollection()
    fun getTrackReleaseDate()
    fun getTrackGenre()
    fun getTrackCountry()
    fun getTrackPreview()
}

package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    val trackName: String, // Название песни
    val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime: Long, // Время песни
    val artworkUrl100: String, // Ссылка на картинку
)

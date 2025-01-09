package com.practicum.playlistmaker.domain.mediateka.model


data class PlayList(
    val id: Int?,
    val playListName: String?,
    val playListDescription: String?,
    val urlImager: String?,
    val tracksIds: MutableList<Int?>,
    var tracksCount: Int?,
)

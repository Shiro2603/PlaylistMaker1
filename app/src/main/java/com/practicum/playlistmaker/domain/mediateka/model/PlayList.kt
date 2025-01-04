package com.practicum.playlistmaker.domain.mediateka.model


data class PlayList(
    val id: Int?,
    val playListName: String?,
    val playListDescription: String?,
    val urlImager: String?,
    val tracksIds: Int?,
    val tracksCount: Int?,
)

package com.practicum.playlistmaker.domain.mediateka.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayList(
    val id: Int?,
    val playListName: String?,
    val playListDescription: String?,
    val urlImager: String?,
    val tracksIds: List<Int?>,
    val tracksCount: Int?,
) : Parcelable

package com.practicum.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val playListName: String?,
    val playListDescription: String?,
    val urlImager: String?,
    val tracksIds: String?,
    var tracksCount: Int?,
    )
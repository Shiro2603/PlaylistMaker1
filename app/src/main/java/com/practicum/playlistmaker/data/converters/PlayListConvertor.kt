package com.practicum.playlistmaker.data.converters

import com.practicum.playlistmaker.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.domain.mediateka.model.PlayList

class PlayListConvertor {

    fun map(playList: PlayList) : PlayListEntity {
        return PlayListEntity(
            id = null,
            playList.playListName,
            playList.playListDescription,
            playList.urlImager,
            playList.tracksIds,
            playList.tracksCount,
        )
    }

    fun map(playList: PlayListEntity) : PlayList {
        return PlayList(
            id = null,
            playList.playListName,
            playList.playListDescription,
            playList.urlImager,
            playList.tracksIds,
            playList.tracksCount,
        )
    }

}
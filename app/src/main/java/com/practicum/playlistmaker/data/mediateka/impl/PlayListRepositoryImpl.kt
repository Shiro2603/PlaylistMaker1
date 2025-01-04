package com.practicum.playlistmaker.data.mediateka.impl

import com.practicum.playlistmaker.data.converters.PlayListConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.mediateka.PlayListRepository
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListConvertor: PlayListConvertor) : PlayListRepository {

    override suspend fun createPlayList(playList: PlayList) {
        val playListEntity = playListConvertor.map(playList)
         appDatabase.playListDao().addPlayList(playListEntity)
    }

    override suspend fun getPlayList(): Flow<List<PlayList>> {
        return appDatabase.playListDao()
            .getPlayList()
            .map { playListEntities ->
                playListEntities.map { playListConvertor.map(it) }
            }
    }

}
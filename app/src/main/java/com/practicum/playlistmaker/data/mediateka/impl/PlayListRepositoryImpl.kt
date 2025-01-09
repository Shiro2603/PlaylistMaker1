package com.practicum.playlistmaker.data.mediateka.impl

import android.graphics.Insets.add
import android.util.Log
import com.practicum.playlistmaker.data.converters.PlayListConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.entity.PlayListTracksEntity
import com.practicum.playlistmaker.data.mediateka.PlayListRepository
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
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

    override suspend fun addToPlayList(track: Track, playList: PlayList) {



        Log.d("Debug", "Before: ${playList.tracksIds}")
        track.trackId.let { playList.tracksIds.add(it) }
        Log.d("Debug", "After: ${playList.tracksIds}")

        Log.d("Debug", "Before: ${playList.tracksCount}")
        playList.tracksCount = playList.tracksCount?.plus(1)
        Log.d("Debug", "After: ${playList.tracksCount}")


        appDatabase.playListDao().updatePlaylist(playListConvertor.map(playList))

    }

}
package com.practicum.playlistmaker.data.mediateka.impl

import com.practicum.playlistmaker.data.converters.PlayListConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.db.entity.PlayListTracksEntity
import com.practicum.playlistmaker.data.mediateka.PlayListRepository
import com.practicum.playlistmaker.domain.mediateka.model.PlayList
import com.practicum.playlistmaker.domain.search.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playListConvertor: PlayListConvertor) : PlayListRepository {

    override suspend fun createPlayList(playList: PlayList) {
        val playListEntity = playListConvertor.map(playList)
         appDatabase.playListDao().addPlayList(playListEntity)
    }

    override fun getPlayList(): Flow<List<PlayList>> {
        return appDatabase.playListDao()
            .getPlayList()
            .map { playListEntities ->
                playListEntities.map { playListConvertor.map(it) }
            }
    }

    override suspend fun addToPlayList(track: Track, playList: PlayList) {
        val tracksIds = playList.tracksIds.plus(track.trackId)
        val newPlayList = playList.copy(
            tracksIds = tracksIds.toMutableList(),
            tracksCount = playList.tracksCount?.inc()
        )
        appDatabase.playListDao().updatePlaylist(playListConvertor.map(newPlayList))
        appDatabase.playListTrackDao().insertTrack(playListConvertor.map(track))
    }

    override suspend fun getPlayListById(playListId: Int): PlayList {
        val playListEntity = appDatabase.playListDao().getPlayListByIds(playListId)
       return playListConvertor.map(playListEntity)
    }

    override fun getTrackForPlayList(trackIds: List<Int?>): Flow<List<Track>> {
        return flow {
            val allTrack = appDatabase.playListTrackDao().getTracks()
            val filteredTracks = allTrack.filter { trackIds.contains(it.trackId) }
            emit(convertFromPlayListTrackEntity(filteredTracks))
        }
    }

    override suspend fun deletePlayListTrack(trackId: Int, playList: PlayList)  {
        val updatedTrackIds = playList.tracksIds.filter { it != trackId }
        val newPlayList = playList.copy(
            tracksIds = updatedTrackIds,
            tracksCount = playList.tracksCount?.dec()
        )

        appDatabase.playListDao().updatePlaylist(playListConvertor.map(newPlayList))

        removeTrackForPlayList(trackId)

    }

    override suspend fun deletePlayList(playList: PlayList) {

        val trackList = playList.tracksIds

        val playListEntity = playListConvertor.map(playList)
        appDatabase.playListDao().deletePlayList(playListEntity)

        trackList.forEach { trackId -> trackId?.let { removeTrackForPlayList(trackId) }
        }

    }

    private suspend fun removeTrackForPlayList(trackID: Int) {

        val isTrackUsedInOtherPlaylists = appDatabase.playListDao().isTrackUsedInOtherPlaylists(trackID)

        if (!isTrackUsedInOtherPlaylists) {
            appDatabase.playListTrackDao().deleteTrack(trackID)

        }
    }

    private fun convertFromPlayListTrackEntity(tracks: List<PlayListTracksEntity>) : List<Track> {
        return tracks.map { track -> playListConvertor.map(track) }
    }

}
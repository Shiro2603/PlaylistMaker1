package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.playlistmaker.data.db.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playList: PlayListEntity)

    @Query("SELECT * FROM playlist_table order by id DESC")
    fun getPlayList() : Flow<List<PlayListEntity>>

    @Update
    suspend fun updatePlaylist(playlist: PlayListEntity)

    @Query("SELECT * FROM playlist_table WHERE id = :playListIds")
    suspend fun getPlayListByIds(playListIds: Int) : PlayListEntity

    @Query("SELECT COUNT(*) > 0 FROM playlist_table WHERE tracksIds = :trackId")
    suspend fun isTrackUsedInOtherPlaylists(trackId: Int) : Boolean

    @Delete
    suspend fun deletePlayList(playList: PlayListEntity)

}
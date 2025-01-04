package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlayListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlayList(playList: PlayListEntity)

    @Query("SELECT * FROM playlist_table order by id DESC")
    fun getPlayList() : Flow<List<PlayListEntity>>
    
}
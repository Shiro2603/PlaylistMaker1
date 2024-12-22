package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.TrackEntity


@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTracks(track: TrackEntity)

    @Query("DELETE FROM track_table where trackId = :trackId")
    suspend fun deleteTracks(trackId: Int)

    @Query("SELECT * FROM track_table order by id desc" )
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
     suspend fun getIdTrack(): List<Int>
}
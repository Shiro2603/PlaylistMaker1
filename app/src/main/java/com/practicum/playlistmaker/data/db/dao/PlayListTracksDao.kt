package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.entity.PlayListTracksEntity

@Dao
interface PlayListTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track : PlayListTracksEntity)

    @Query("SELECT * FROM playlist_tracks order by orderTime desc" )
    suspend fun getTracks(): List<PlayListTracksEntity>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int)

}
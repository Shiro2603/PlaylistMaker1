package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.PlayListDao
import com.practicum.playlistmaker.data.db.dao.TrackDao
import com.practicum.playlistmaker.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlayListEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playListDao(): PlayListDao

}
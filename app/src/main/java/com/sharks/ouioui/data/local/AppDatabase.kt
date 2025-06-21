package com.sharks.ouioui.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sharks.ouioui.data.model.Destination

@Database(entities = [Destination::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDestinationDao(): FavoriteDestinationDao
}
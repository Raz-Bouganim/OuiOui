package com.sharks.ouioui.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sharks.ouioui.data.model.Destination

/**
 * Main Room database for storing favorite destinations.
 * Version 2 includes "Title" as primary key.
 */
@Database(entities = [Destination::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDestinationDao(): FavoriteDestinationDao
}
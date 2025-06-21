package com.sharks.ouioui.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sharks.ouioui.data.model.Destination

@Dao
interface FavoriteDestinationDao {
    @Query("SELECT * FROM favorite_destinations")
    fun getAllFavorites(): LiveData<List<Destination>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(destination: Destination)

    @Delete
    suspend fun deleteFavorite(destination: Destination)
}
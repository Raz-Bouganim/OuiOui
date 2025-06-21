package com.sharks.ouioui.data.repository

import androidx.lifecycle.LiveData
import com.sharks.ouioui.data.local.FavoriteDestinationDao
import com.sharks.ouioui.data.model.Destination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(private val dao: FavoriteDestinationDao) {
    val allFavorites: LiveData<List<Destination>> = dao.getAllFavorites()

    suspend fun addFavorite(destination: Destination) = dao.insertFavorite(destination)
    suspend fun removeFavorite(destination: Destination) = dao.deleteFavorite(destination)
}
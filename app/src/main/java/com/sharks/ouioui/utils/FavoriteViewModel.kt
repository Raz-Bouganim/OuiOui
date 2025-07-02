package com.sharks.ouioui.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing favorite destinations.
 */
@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavoriteRepository) : ViewModel() {

    val favorites: LiveData<List<Destination>> = repository.allFavorites

    fun addFavorite(destination: Destination) = viewModelScope.launch {
        repository.addFavorite(destination)
    }

    fun removeFavorite(destination: Destination) = viewModelScope.launch {
        repository.removeFavorite(destination)
    }

    fun toggleFavorite(destination: Destination) {
        val current = favorites.value ?: emptyList()
        if (current.any { it.title == destination.title }) {
            removeFavorite(destination)
        } else {
            addFavorite(destination)
        }
    }
}
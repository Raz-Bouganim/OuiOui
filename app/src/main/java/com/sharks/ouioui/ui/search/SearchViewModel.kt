package com.sharks.ouioui.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.repository.DestinationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for searching travel destinations.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: DestinationRepo): ViewModel() {

    private val _destinations = MutableLiveData<List<Destination>>()
    val destinations: LiveData<List<Destination>> = _destinations

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    var lastQuery: String? = null
        private set
    val lastQueryValue: String?
        get() = lastQuery

    /**
     * Fetches destinations for the given query if it differs from the last query.
     */
    fun fetchDestinations(query: String) {
        if (query != lastQuery) {
            lastQuery = query
            viewModelScope.launch {
                try {
                    val result = repository.getPopularDestinations(query)
                    _destinations.value = result
                } catch (e: Exception) {
                    _error.value = "Error: ${e.message}"
                }
            }
        }
    }

    /**
     * Toggles the favorite status of a destination in the current search results.
     */
    fun toggleFavorite(destination: Destination) {
        val updatedList = _destinations.value?.map {
            if (it.title == destination.title) it.copy(isFavorite = !it.isFavorite) else it
        }
        _destinations.value = updatedList ?: emptyList()
    }
}
package com.sharks.ouioui.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.repository.DestinationRepo
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val repository = DestinationRepo()

    private val _destinations = MutableLiveData<List<Destination>>()
    val destinations: LiveData<List<Destination>> = _destinations

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchDestinations(query: String) {
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
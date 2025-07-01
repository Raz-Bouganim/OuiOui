package com.sharks.ouioui.ui.events

import androidx.lifecycle.*
import com.sharks.ouioui.data.model.Event
import com.sharks.ouioui.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing events in a specific city.
 * Fetches events from the repository and exposes them as LiveData.
 */
@HiltViewModel
class EventsViewModel @Inject constructor(private val repo: EventsRepository) : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchEvents(city: String) {
        viewModelScope.launch {
            try {
                _events.value = repo.getEventsForCity(city)
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}
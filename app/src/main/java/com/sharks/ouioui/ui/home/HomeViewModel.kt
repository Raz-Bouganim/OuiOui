package com.sharks.ouioui.ui.home

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
 * ViewModel for the Home screen.
 * Handles fetching and exposing destination data for the UI.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: DestinationRepo) : ViewModel() {
    private val _worldDestinations = MutableLiveData<List<Destination>>()
    val worldDestinations: LiveData<List<Destination>> = _worldDestinations

    private val _locationDestinations = MutableLiveData<List<Destination>>()
    val locationDestinations: LiveData<List<Destination>> = _locationDestinations

    private val _loadingWorld = MutableLiveData<Boolean>()
    val loadingWorld: LiveData<Boolean> = _loadingWorld

    private val _loadingLocationDestinations = MutableLiveData<Boolean>()
    val loadingLocationDestinations: LiveData<Boolean> = _loadingLocationDestinations

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val countryList = listOf(
        "France", "Italy", "Spain", "Germany", "United Kingdom", "United States",
        "Canada", "Australia", "Israel", "Japan", "Greece", "Portugal", "China",
        "India", "Brazil", "Mexico", "South Africa", "Turkey", "Russia", "Netherlands"
    )

    /**
     * Fetches a shuffled list of popular destinations from random countries.
     */
    fun fetchWorldDestinations() {
        if (_worldDestinations.value.isNullOrEmpty()) {
            _loadingWorld.value = true
            viewModelScope.launch {
                try {
                    val randomCountry = countryList.shuffled().take(10)
                    val allDestinations = mutableListOf<Destination>()
                    for (country in randomCountry) {
                        val result = repository.getPopularDestinations(country)
                        if (result.isNotEmpty()) {
                            allDestinations.addAll(result)
                        }
                    }
                    _worldDestinations.value = allDestinations.shuffled()
                } catch (e: Exception) {
                    _error.value = "Error: ${e.message}"
                }
                finally {
                    _loadingWorld.value = false
                }
            }
        }
    }

    /**
     * Fetches popular destinations for a specific country.
     */
    fun fetchDestinationsForCountry(country: String) {
        _loadingLocationDestinations.value = true
        viewModelScope.launch {
            try {
                val result = repository.getPopularDestinations(country)
                _locationDestinations.value = result.shuffled()
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _loadingLocationDestinations.value = false
            }
        }
    }
}

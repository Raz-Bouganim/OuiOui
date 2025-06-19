package com.sharks.ouioui.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.repository.DestinationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: DestinationRepo) : ViewModel() {

    private val _worldDestinations = MutableLiveData<List<Destination>>()
    val worldDestinations: LiveData<List<Destination>> = _worldDestinations
    private val countryList = listOf(
        "France", "Italy", "Spain", "Germany", "United Kingdom", "United States",
        "Canada", "Australia", "Israel", "Japan", "Greece", "Portugal", "China",
        "India", "Brazil", "Mexico", "South Africa", "Turkey", "Russia", "Netherlands"
    )

    private val _franceDestinations = MutableLiveData<List<Destination>>()
    val franceDestinations: LiveData<List<Destination>> = _franceDestinations

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchWorldDestinations() {
        if (_worldDestinations.value.isNullOrEmpty()) {
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
            }
        }
    }

    fun fetchFranceDestinations() {
        if (_franceDestinations.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val result = repository.getPopularDestinations("France")
                    _franceDestinations.value = result.shuffled()
                } catch (e: Exception) {
                    _error.value = "Error: ${e.message}"
                }
            }
        }
    }
}

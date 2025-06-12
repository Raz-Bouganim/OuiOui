package com.sharks.ouioui.repository

import android.util.Log
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.data.remote.RetrofitInstance

class DestinationRepo() {
    suspend fun getPopularDestinations(query: String): List<Destination> {
        val apiKey = "1ddc64c217040535b769e34b27849877e555e88d7ca0bb42180dc7f39f133fd8"
        val response = RetrofitInstance.api.getPopularDestinations(query = query, apiKey = apiKey)
        Log.d("DestinationRepo", "Raw API response: $response")
        return response.popular_destinations?.destinations?.map {
            Destination(
                title = it.title ?: "No title",
                link = it.link ?: "No link",
                description = it.description ?: "No description",
                thumbnail = it.thumbnail ?: "No thumbnail"
            )
        } ?: emptyList()
    }
}
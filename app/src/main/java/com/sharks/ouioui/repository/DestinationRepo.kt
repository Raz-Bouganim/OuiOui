package com.sharks.ouioui.repository

import android.util.Log
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.data.remote.RetrofitInstance
import com.sharks.ouioui.utils.Constants.Companion.API_KEY

class DestinationRepo() {
    suspend fun getPopularDestinations(query: String): List<Destination> {
        val response = RetrofitInstance.api.getPopularDestinations(query = query, apiKey = API_KEY)
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
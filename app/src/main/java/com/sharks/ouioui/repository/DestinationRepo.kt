package com.sharks.ouioui.repository

import android.content.Context
import com.sharks.ouioui.R
import com.sharks.ouioui.data.model.Destination
import com.sharks.ouioui.data.remote.ApiService
import com.sharks.ouioui.utils.Constants.Companion.API_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Repository for fetching popular destinations from the remote API.
 * Handles mapping API responses to local objects(Destination) with default values.
 */
class DestinationRepo @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {
    suspend fun getPopularDestinations(query: String): List<Destination> {
        val response = apiService.getPopularDestinations(query = query, apiKey = API_KEY)
        return response.popular_destinations?.destinations?.map {
            Destination(
                title = it.title,
                link = it.link ?: context.getString(R.string.noLinkText),
                description = it.description ?: context.getString(R.string.noDescriptionText),
                thumbnail = it.thumbnail ?: context.getString(R.string.noThumbnailText),
                isFavorite = false
            )
        } ?: emptyList()
    }
}
package com.sharks.ouioui.repository

import android.content.Context
import com.sharks.ouioui.data.model.Event
import com.sharks.ouioui.data.remote.ApiService
import com.sharks.ouioui.utils.Constants.Companion.API_KEY
import javax.inject.Inject
import com.sharks.ouioui.data.model.EventDate
import com.sharks.ouioui.R
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Repository for fetching upcoming events in a requested city.
 * Handles mapping API responses to local objects(Event) with default values.
 */
class EventsRepository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {
    suspend fun getEventsForCity(city: String): List<Event> {
        val response = apiService.getEvents(query = "events in $city", apiKey = API_KEY)
        return response.events_results?.map {
            Event(
                title = it.title,
                date = it.date ?: EventDate(start_date = null, when_ = null),
                address = it.address ?: emptyList(),
                link = it.link ?: context.getString(R.string.noLinkText),
                thumbnail = it.thumbnail ?: context.getString(R.string.noThumbnailText)
            )
        } ?: emptyList()
    }
}
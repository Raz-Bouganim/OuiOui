package com.sharks.ouioui.data.model

import com.google.gson.annotations.SerializedName

/**
 * Api response wrapper for events.
 */
data class EventApiResponse(
    val events_results: List<Event>?
)

/**
 * Represents an event with its details.
 */
data class Event(
    val title: String,
    val date: EventDate?,
    val address: List<String>?,
    val link: String?,
    val thumbnail: String?
)

/**
 * Contains the date and time information for an event.
 */
data class EventDate(
    val start_date: String?,
    @SerializedName("when")
    val when_: String?
)
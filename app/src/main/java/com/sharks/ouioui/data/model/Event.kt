package com.sharks.ouioui.data.model

import com.google.gson.annotations.SerializedName

data class EventApiResponse(
    val events_results: List<Event>?
)

data class Event(
    val title: String,
    val date: EventDate?,
    val address: List<String>?,
    val link: String?,
    val thumbnail: String?
)

data class EventDate(
    val start_date: String?,
    @SerializedName("when")
    val when_: String?
)
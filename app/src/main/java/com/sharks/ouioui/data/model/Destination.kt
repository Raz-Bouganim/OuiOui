package com.sharks.ouioui.data.model

data class ApiResponse(
    val popular_destinations: PopularDestinations?
)

data class PopularDestinations(
    val destinations: List<Destination>?
)

data class Destination(
    val title: String?,
    val link: String?,
    val description: String?,
    val thumbnail: String?,
)
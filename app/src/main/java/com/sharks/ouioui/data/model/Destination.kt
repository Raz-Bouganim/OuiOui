package com.sharks.ouioui.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Api response wrapper for popular destinations.
 */
data class DestinationResponse(
    val popular_destinations: PopularDestinations?
)

/**
 * Contains a list of Destination objects.
 */
data class PopularDestinations(
    val destinations: List<Destination>?
)

/**
 * Represents a destination with its details.
 * Used for displaying in the UI and storing in the local database.
 */
@Entity(tableName = "favorite_destinations")
data class Destination(
    @PrimaryKey val title: String,
    val link: String?,
    val description: String?,
    val thumbnail: String?,
    val isFavorite: Boolean = false
)
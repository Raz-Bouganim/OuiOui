package com.sharks.ouioui.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ApiResponse(
    val popular_destinations: PopularDestinations?
)

data class PopularDestinations(
    val destinations: List<Destination>?
)

@Entity(tableName = "favorite_destinations")
data class Destination(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String?,
    val link: String?,
    val description: String?,
    val thumbnail: String?,
    val isFavorite: Boolean = false
)
package com.sharks.ouioui.data.remote

import com.sharks.ouioui.data.model.DestinationResponse
import com.sharks.ouioui.data.model.EventApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API service for fetching popular travel destinations and events.
 * Based on SerpApi(Google Popular Destinations API, Google Events Results API) documentation.
 */
interface ApiService {
    @GET("search.json")
    suspend fun getPopularDestinations(
        @Query("engine") engine: String = "google",
        @Query("q") query: String,
        @Query("api_key") apiKey: String
    ): DestinationResponse

    @GET("search.json")
    suspend fun getEvents(
        @Query("engine") engine: String = "google_events",
        @Query("q") query: String,
        @Query("api_key") apiKey: String
    ): EventApiResponse
}
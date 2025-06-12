package com.sharks.ouioui.data.remote

import com.sharks.ouioui.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search.json")
    suspend fun getPopularDestinations(
        @Query("engine") engine: String = "google",
        @Query("q") query: String,
        @Query("api_key") apiKey: String
    ): ApiResponse
}

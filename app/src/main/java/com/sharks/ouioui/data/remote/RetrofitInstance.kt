package com.sharks.ouioui.data.remote

import com.sharks.ouioui.utils.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object for providing a configured Retrofit API service instance.
 * Uses the base URL from Constants and Gson for JSON conversion.
 */
object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
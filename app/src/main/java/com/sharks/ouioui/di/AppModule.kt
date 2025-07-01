package com.sharks.ouioui.di

import android.content.Context
import com.sharks.ouioui.data.remote.ApiService
import com.sharks.ouioui.data.remote.RetrofitInstance
import com.sharks.ouioui.repository.DestinationRepo
import com.sharks.ouioui.repository.EventsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing network and repository dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitInstance.api

    @Provides
    @Singleton
    fun provideDestinationRepo(
        apiService: ApiService,
        @ApplicationContext context: Context
    ): DestinationRepo = DestinationRepo(apiService, context)

    @Provides
    @Singleton
    fun provideEventsRepository(
        apiService: ApiService,
        @ApplicationContext context: Context
    ): EventsRepository = EventsRepository(apiService, context)
}
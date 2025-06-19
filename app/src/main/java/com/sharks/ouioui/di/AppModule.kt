package com.sharks.ouioui.di

import com.sharks.ouioui.data.remote.ApiService
import com.sharks.ouioui.data.remote.RetrofitInstance
import com.sharks.ouioui.repository.DestinationRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService = RetrofitInstance.api

    @Provides
    @Singleton
    fun provideDestinationRepo(apiService: ApiService): DestinationRepo = DestinationRepo(apiService)
}
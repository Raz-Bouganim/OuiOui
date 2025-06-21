package com.sharks.ouioui.di

import android.content.Context
import androidx.room.Room
import com.sharks.ouioui.data.local.AppDatabase
import com.sharks.ouioui.data.local.FavoriteDestinationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "app_database").build()

    @Provides
    fun provideFavoriteDestinationDao(db: AppDatabase): FavoriteDestinationDao =
        db.favoriteDestinationDao()
}
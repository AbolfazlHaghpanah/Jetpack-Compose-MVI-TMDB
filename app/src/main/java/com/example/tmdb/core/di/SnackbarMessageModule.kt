package com.example.tmdb.core.di

import com.example.tmdb.core.utils.SnackBarManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TMDBSnackBar {

    @Singleton
    @Provides
    fun provideSnackBar(): SnackBarManager {
        return SnackBarManager()
    }
}

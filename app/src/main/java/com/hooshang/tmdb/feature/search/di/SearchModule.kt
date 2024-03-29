package com.hooshang.tmdb.feature.search.di

import com.hooshang.tmdb.core.data.db.AppDatabase
import com.hooshang.tmdb.feature.search.data.datasource.localdatasource.SearchLocalDataSource
import com.hooshang.tmdb.feature.search.data.datasource.localdatasource.SearchLocalDataSourceImpl
import com.hooshang.tmdb.feature.search.data.datasource.remotedatasource.SearchRemoteDataSource
import com.hooshang.tmdb.feature.search.data.datasource.remotedatasource.SearchRemoteDataSourceImpl
import com.hooshang.tmdb.feature.search.data.db.dao.SearchDao
import com.hooshang.tmdb.feature.search.data.network.api.SearchApi
import com.hooshang.tmdb.feature.search.data.repository.SearchRepositoryImpl
import com.hooshang.tmdb.feature.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {
    @Binds
    abstract fun bindsSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    abstract fun bindsSearchLocalDataSource(
        searchLocalDataSourceImpl: SearchLocalDataSourceImpl
    ): SearchLocalDataSource

    @Binds
    abstract fun bindsSearchRemoteDataSource(
        searchRemoteDataSourceImpl: SearchRemoteDataSourceImpl
    ): SearchRemoteDataSource

    companion object {
        @Provides
        fun provideSearchApi(retrofit: Retrofit): SearchApi {
            return retrofit.create(SearchApi::class.java)
        }

        @Provides
        fun provideSearchDao(appDatabase: AppDatabase): SearchDao {
            return appDatabase.SearchDao()
        }
    }
}
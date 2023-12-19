package com.hooshang.tmdb.feature.home.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hooshang.tmdb.core.data.model.local.GenreEntity
import com.hooshang.tmdb.feature.home.data.model.local.entity.NowPlayingEntity
import com.hooshang.tmdb.feature.home.data.model.local.relation.NowPlayingWithMovie
import com.hooshang.tmdb.feature.home.data.model.local.entity.PopularMovieEntity
import com.hooshang.tmdb.feature.home.data.model.local.relation.PopularMovieAndGenreWithMovie
import com.hooshang.tmdb.feature.home.data.model.local.relation.crossref.PopularMovieGenreCrossRef
import com.hooshang.tmdb.feature.home.data.model.local.entity.TopMovieEntity
import com.hooshang.tmdb.feature.home.data.model.local.relation.TopMovieAndGenreWithMovie
import com.hooshang.tmdb.feature.home.data.model.local.relation.crossref.TopMovieGenreCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Query("SELECT * FROM NOW_PLAYING")
    fun observeNowPlayingMovie(): Flow<List<NowPlayingWithMovie>>
    @Query("SELECT * FROM POPULAR_MOVIES")
    fun observePopularMovie(): Flow<List<PopularMovieAndGenreWithMovie>>

    @Query("SELECT * FROM TOP_MOVIE")
    fun observeTopMovie(): Flow<List<TopMovieAndGenreWithMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGenre(genre: List<GenreEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNowPlayingMovie(movie: List<NowPlayingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPopularMovie(movie: List<PopularMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPopularMoviesGenre(genre: List<PopularMovieGenreCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTopMovie(movie: List<TopMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTopMoviesGenre(genre: List<TopMovieGenreCrossRef>)

    @Query("DELETE FROM now_playing")
    fun removeNowPlayingMovies()

    @Query("DELETE FROM popularmoviegenrecrossref")
    fun removePopularMoviesGenre()

    @Query("DELETE FROM popular_movies")
    fun removePopularMovies()

    @Query("DELETE FROM top_movie")
    fun removeTopMovies()

    @Query("DELETE FROM topmoviegenrecrossref")
    fun removeTopMovieGenre()
}
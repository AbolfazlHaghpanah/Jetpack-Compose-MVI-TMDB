package com.example.tmdb.feature.home.data.popularMovie.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tmdb.core.data.moviedata.Entity.MovieEntity
import com.example.tmdb.core.utils.MovieDatabaseWrapper
import com.example.tmdb.core.utils.MovieWithGenreDatabaseWrapper
import com.example.tmdb.core.data.genre.entity.GenreEntity
import com.example.tmdb.feature.home.data.popularMovie.PopularMovieEntity
import kotlinx.collections.immutable.toPersistentList

data class PopularMovieAndGenreWithMovie(
    @Embedded val popularMovie: PopularMovieEntity,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "id",
    )
    val movie: MovieEntity,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(PopularMovieGenreCrossRef::class)
    )
    val genres: List<GenreEntity>
) {
    fun toMovieDataWrapper(): MovieWithGenreDatabaseWrapper {

        return MovieWithGenreDatabaseWrapper(
            genres = genres.toPersistentList(),
            movie = MovieDatabaseWrapper(
                movieId = movie.id,
                title = movie.title,
                posterPath = movie.posterPath ,
                voteAverage = movie.voteAverage
            )
        )
    }
}
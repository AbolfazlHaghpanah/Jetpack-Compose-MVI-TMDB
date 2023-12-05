package com.example.tmdb.feature.home.data.model.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.tmdb.core.data.model.local.GenreEntity
import com.example.tmdb.core.data.model.local.MovieEntity
import com.example.tmdb.feature.home.data.model.local.entity.PopularMovieEntity
import com.example.tmdb.feature.home.data.model.local.relation.crossref.PopularMovieGenreCrossRef
import com.example.tmdb.feature.home.domain.model.HomeMovieDomainModel

data class PopularMovieAndGenreWithMovie(
    @Embedded val popularMovie: PopularMovieEntity?,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "id",
    )
    val movie: MovieEntity?,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(PopularMovieGenreCrossRef::class)
    )
    val genres: List<GenreEntity>?
) {
    fun toDomainModel(): HomeMovieDomainModel {
        return HomeMovieDomainModel(
            genres = genres?.joinToString(separator = "|") { it.genreName }?:"",
            movieId = movie?.id ?: 1,
            title = movie?.title ?: "",
            posterPath = movie?.posterPath ?: "",
            voteAverage = movie?.voteAverage ?: 0.0,
            releaseDate = "",
            backdropPath = movie?.backdropPath ?: ""
        )
    }
}
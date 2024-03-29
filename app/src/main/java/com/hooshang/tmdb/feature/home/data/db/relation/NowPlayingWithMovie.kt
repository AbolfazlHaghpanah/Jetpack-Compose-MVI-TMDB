package com.hooshang.tmdb.feature.home.data.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.hooshang.tmdb.core.data.db.entity.MovieEntity
import com.hooshang.tmdb.feature.home.data.db.entity.NowPlayingEntity
import com.hooshang.tmdb.feature.home.domain.model.HomeMovieDomainModel

data class NowPlayingWithMovie(
    @Embedded val nowPlayingMovie: NowPlayingEntity?,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "id"
    )
    val movie: MovieEntity?,
) {
    fun toDomainModel(): HomeMovieDomainModel = HomeMovieDomainModel(
        genres = "",
        movieId = movie?.id ?: 1,
        title = movie?.title ?: "",
        posterPath = movie?.posterPath ?: "",
        voteAverage = movie?.voteAverage ?: 0.0,
        releaseDate = nowPlayingMovie?.releaseDate ?: "",
        backdropPath = movie?.backdropPath ?: ""
    )
}
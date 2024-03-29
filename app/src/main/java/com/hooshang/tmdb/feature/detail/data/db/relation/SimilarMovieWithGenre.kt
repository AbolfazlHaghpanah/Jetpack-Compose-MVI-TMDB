package com.hooshang.tmdb.feature.detail.data.db.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hooshang.tmdb.core.data.db.entity.GenreEntity
import com.hooshang.tmdb.core.data.db.entity.MovieEntity
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.MovieWithGenreCrossRef
import com.hooshang.tmdb.feature.detail.domain.model.SimilarMovieDomainModel

data class SimilarMovieWithGenre(
    @Embedded val similarMovie: MovieEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "genreId",
        entity = GenreEntity::class,
        associateBy = Junction(MovieWithGenreCrossRef::class)
    )
    val genres: List<GenreEntity>
) {
    fun toDomainModel(): SimilarMovieDomainModel = SimilarMovieDomainModel(
        id = similarMovie.id,
        posterPath = similarMovie.posterPath,
        voteAverage = similarMovie.voteAverage.toFloat(),
        title = similarMovie.title,
        genres = genres.joinToString(separator = "|") { it.genreName }
    )
}
package com.hooshang.tmdb.feature.detail.domain.model

data class MovieDetailDomainModel(
    val id: Int,
    val title: String,
    val overview: String,
    val voteAverage: Double,
    val posterPath: String,
    val releaseDate: String,
    val runtime: Int,
    val genres: List<Pair<Int, String>>,
    val externalIds: List<String>,
    val credits: List<CastOrCrewDomainModel>,
    val similar: List<SimilarMovieDomainModel>,
    val isFavorite: Boolean
)

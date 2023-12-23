package com.hooshang.tmdb.feature.detail.data.repository

import com.hooshang.tmdb.core.data.model.local.MovieEntity
import com.hooshang.tmdb.feature.detail.data.datasource.localdatasource.DetailLocalDataSource
import com.hooshang.tmdb.feature.detail.data.datasource.remotedatasource.DetailRemoteDataSource
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.DetailMovieWithCreditCrossRef
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.DetailMovieWithGenreCrossRef
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.DetailMovieWithSimilarMoviesCrossRef
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.MovieWithGenreCrossRef
import com.hooshang.tmdb.feature.detail.domain.model.MovieDetailDomainModel
import com.hooshang.tmdb.feature.detail.domain.repository.DetailRepository
import com.hooshang.tmdb.feature.favorite.data.model.local.entity.FavoriteMovieEntity
import com.hooshang.tmdb.feature.favorite.data.model.local.relation.FavoriteMovieGenreCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val localDataSource: DetailLocalDataSource,
    private val remoteDataSource: DetailRemoteDataSource
) : DetailRepository {
    override suspend fun observeMovieDetails(id: Int): Flow<MovieDetailDomainModel> {
        val dataFlow = localDataSource.observeMovieDetail(id)
        val isFavoriteFlow = localDataSource.isExistInFavorite(id)
        val creditsFlow = localDataSource.observeCredits(id)
        val similarFlow = localDataSource.observeSimilar(id)

        return combine(
            dataFlow,
            isFavoriteFlow,
            creditsFlow,
            similarFlow
        ) { data, isFavorite, credits, similar ->
            data?.toDomainModel()?.copy(
                isFavorite = isFavorite,
                credits = credits.map { it.toDomainModel() },
                similar = similar.map { it.toDomainModel() }
            )
        }.filterNotNull()
    }

    override suspend fun addToFavorite(movieId: Int, genres: List<Int>) {
            localDataSource.insertFavoriteMovieGenre(
                genres.map {
                    FavoriteMovieGenreCrossRef(
                        movieId = movieId,
                        genreId = it
                    )
                }
            )
            localDataSource.addToFavorite(FavoriteMovieEntity(movieId))
        }

    override suspend fun fetchMovieDetail(id: Int) {
        val movieDetailDto = remoteDataSource.getMovieDetail(id)

        localDataSource.insertMovies(listOf(movieDetailDto.toMovieEntity()))

        localDataSource.insertMovieDetails(movieDetailDto.toDetailEntity())

        localDataSource.insertCredits(movieDetailDto.toCreditsEntity())

        localDataSource.insertDetailMoviesWithCredits(
            movieDetailDto.credits.cast.map {
                DetailMovieWithCreditCrossRef(
                    detailMovieId = movieDetailDto.id,
                    creditId = it.id
                )
            } + movieDetailDto.credits.crew.map {
                DetailMovieWithCreditCrossRef(
                    detailMovieId = movieDetailDto.id,
                    creditId = it.id
                )
            }
        )

        localDataSource.insertDetailMoviesWithGenres(
            movieDetailDto.genreResponses.map {
                DetailMovieWithGenreCrossRef(
                    detailMovieId = movieDetailDto.id,
                    genreId = it.id
                )
            }
        )

        localDataSource.insertDetailMoviesWithSimilarMovies(
            movieDetailDto.similar.results.map {
                DetailMovieWithSimilarMoviesCrossRef(
                    detailMovieId = movieDetailDto.id,
                    id = it.id
                )
            }
        )

        localDataSource.insertMovies(
            movieDetailDto.similar.results.map { similarMovieResult ->
                localDataSource.insertMoviesWithGenres(
                    similarMovieResult.genreIds.map {
                        MovieWithGenreCrossRef(
                            id = similarMovieResult.id,
                            genreId = it
                        )
                    }
                )
                MovieEntity(
                    id = similarMovieResult.id,
                    title = similarMovieResult.title,
                    backdropPath = similarMovieResult.backdropPath ?: "",
                    voteAverage = similarMovieResult.voteAverage.toDouble(),
                    posterPath = similarMovieResult.posterPath ?: ""
                )
            }
        )
    }
}

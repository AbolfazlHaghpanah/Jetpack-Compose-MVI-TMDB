package com.hooshang.tmdb.feature.detail.data.datasource.localdatasource

import com.hooshang.tmdb.core.data.db.entity.MovieEntity
import com.hooshang.tmdb.core.data.db.dao.MovieDao
import com.hooshang.tmdb.feature.detail.data.db.dao.DetailDao
import com.hooshang.tmdb.feature.detail.data.db.entity.CreditEntity
import com.hooshang.tmdb.feature.detail.data.db.entity.DetailEntity
import com.hooshang.tmdb.feature.detail.data.db.relation.DetailMovieWithAllRelations
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.DetailMovieWithCreditCrossRef
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.DetailMovieWithGenreCrossRef
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.DetailMovieWithSimilarMoviesCrossRef
import com.hooshang.tmdb.feature.detail.data.db.relation.crossrefrence.MovieWithGenreCrossRef
import com.hooshang.tmdb.feature.favorite.data.db.entity.FavoriteMovieEntity
import com.hooshang.tmdb.feature.favorite.data.db.relation.FavoriteMovieGenreCrossRef
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailLocalDataSourceImpl  @Inject constructor(
    private val detailDao: DetailDao,
    private val movieDao: MovieDao
) : DetailLocalDataSource {
    override fun getMovieDetail(detailMovieId: Int): DetailMovieWithAllRelations? =
        detailDao.getMovieDetail(detailMovieId)

    override fun observeExistInFavorite(id: Int): Flow<Boolean> =
        detailDao.observeExistInFavorite(id)

    override suspend fun insertMovieDetails(detailEntity: DetailEntity) =
        detailDao.insertMovieDetails(detailEntity)

    override suspend fun insertDetailMoviesWithGenres(detailMovieWithGenreCrossRef: List<DetailMovieWithGenreCrossRef>) =
        detailDao.insertDetailMoviesWithGenres(detailMovieWithGenreCrossRef)

    override suspend fun insertFavoriteMovieGenres(genres: List<FavoriteMovieGenreCrossRef>) =
        detailDao.insertFavoriteMovieGenres(genres)

    override suspend fun insertDetailMoviesWithSimilarMovies(detailMovieWithSimilarMoviesCrossRef: List<DetailMovieWithSimilarMoviesCrossRef>) =
        detailDao.insertDetailMoviesWithSimilarMovies(detailMovieWithSimilarMoviesCrossRef)

    override suspend fun insertMoviesWithGenres(movieWithGenre: List<MovieWithGenreCrossRef>) =
        detailDao.insertMoviesWithGenres(movieWithGenre)

    override suspend fun insertCredits(credit: List<CreditEntity>) =
        detailDao.insertCredits(credit)

    override suspend fun insertDetailMoviesWithCredits(detailMovieWithCreditCrossRef: List<DetailMovieWithCreditCrossRef>) =
        detailDao.insertDetailMoviesWithCredits(detailMovieWithCreditCrossRef)

    override suspend fun insertToFavorite(movieEntity: FavoriteMovieEntity) =
        detailDao.insertToFavorite(movieEntity)

    override suspend fun insertMovies(movie: List<MovieEntity>) =
        movieDao.insertMovies(movie)

}
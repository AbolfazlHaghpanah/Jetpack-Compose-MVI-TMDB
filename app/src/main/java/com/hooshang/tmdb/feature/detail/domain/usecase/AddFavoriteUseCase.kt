package com.hooshang.tmdb.feature.detail.domain.usecase

import com.hooshang.tmdb.feature.detail.domain.repository.DetailRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(movieId: Int, genres: List<Int>) {
        detailRepository.addToFavorite(movieId, genres)
    }
}
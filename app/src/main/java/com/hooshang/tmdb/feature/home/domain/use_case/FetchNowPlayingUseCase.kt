package com.hooshang.tmdb.feature.home.domain.use_case

import com.hooshang.tmdb.feature.home.domain.repository.HomeRepository

class FetchNowPlayingUseCase (
    private val homeRepository: HomeRepository
){

    suspend operator fun invoke(){
        homeRepository.fetchNowPlaying()
    }
}
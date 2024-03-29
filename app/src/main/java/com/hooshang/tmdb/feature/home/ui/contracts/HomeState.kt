package com.hooshang.tmdb.feature.home.ui.contracts

import com.hooshang.tmdb.core.ui.ViewState
import com.hooshang.tmdb.feature.home.domain.model.HomeMovieDomainModel

data class HomeState(
    val nowPlayingMovies: List<HomeMovieDomainModel> = listOf(),
    val popularMovies: List<HomeMovieDomainModel> = listOf(),
    val topRatedMovies: List<HomeMovieDomainModel> = listOf(),
    val isLoading: Boolean = false
) : ViewState
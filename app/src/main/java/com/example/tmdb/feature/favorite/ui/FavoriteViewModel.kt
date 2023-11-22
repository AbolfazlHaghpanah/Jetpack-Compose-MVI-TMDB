package com.example.tmdb.feature.favorite.ui

import androidx.compose.material.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb.core.data.databaseErrorCatchMessage
import com.example.tmdb.core.utils.SnackBarMessage
import com.example.tmdb.feature.favorite.data.FavoriteMovieDao
import com.example.tmdb.feature.home.data.common.MovieWithGenreDatabaseWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val snackBarMessage: SnackBarMessage
) : ViewModel() {

    private val _favoriteMovieList =
        MutableStateFlow<List<MovieWithGenreDatabaseWrapper>>(emptyList())

    val favoriteMovieList = _favoriteMovieList.asStateFlow()

    init {
        observeFavoriteMovies()
    }

    private fun onTryAgain() {
        observeFavoriteMovies()
    }

    private fun observeFavoriteMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteMovieDao.observeMovies()
                .catch {
                    snackBarMessage.sendMessage(
                        message = databaseErrorCatchMessage(it),
                        duration = SnackbarDuration.Indefinite,
                        action = {
                            onTryAgain()
                        },
                        actionLabel = "Try Again"
                    )
                }.collect {
                    _favoriteMovieList.emit(it.map { movie -> movie.toMovieDatabaseWrapper() })
                }
        }
    }
}
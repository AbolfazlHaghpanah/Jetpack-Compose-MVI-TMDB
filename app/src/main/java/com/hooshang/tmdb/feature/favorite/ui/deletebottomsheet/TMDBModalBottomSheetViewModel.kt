package com.hooshang.tmdb.feature.favorite.ui.deletebottomsheet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hooshang.tmdb.core.ui.BaseViewModel
import com.hooshang.tmdb.core.utils.SnackBarManager
import com.hooshang.tmdb.core.utils.SnackBarMassage
import com.hooshang.tmdb.core.utils.databaseErrorCatchMessage
import com.hooshang.tmdb.feature.favorite.domain.use_case.RemoveFavoriteUseCase
import com.hooshang.tmdb.feature.favorite.ui.deletebottomsheet.contracts.DeleteBottomSheetAction
import com.hooshang.tmdb.feature.favorite.ui.deletebottomsheet.contracts.DeleteBottomSheetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TMDBModalBottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val snackBarManager: SnackBarManager
) : BaseViewModel<DeleteBottomSheetAction, DeleteBottomSheetState>() {
    init {
        setState { copy(id = savedStateHandle.get<String>("id")?.toIntOrNull() ?: -1) }
    }

    private fun deleteMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                removeFavoriteUseCase(state.value.id)
            } catch (t: Throwable) {
                snackBarManager.sendMessage(SnackBarMassage(databaseErrorCatchMessage(t)))
            }
        }
    }

    override fun onAction(action: DeleteBottomSheetAction) {
        when (action) {
            is DeleteBottomSheetAction.DeleteFromFavorite -> deleteMovie()
            else -> {}
        }
    }

    override fun setInitialState(): DeleteBottomSheetState = DeleteBottomSheetState()
}
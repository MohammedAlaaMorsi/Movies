package io.mohammedalaamorsi.movies.presentation.states.effects

import androidx.annotation.StringRes

sealed interface Effect {
    data class ShowSnackbarResource(@StringRes val messageRes: Int): Effect
}

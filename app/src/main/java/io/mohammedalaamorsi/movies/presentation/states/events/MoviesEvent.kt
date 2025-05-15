package io.mohammedalaamorsi.movies.presentation.states.events

import androidx.annotation.StringRes


sealed interface MoviesEvent {
    data object GetPopularMovies : MoviesEvent
    data class OnSearch(val searchQuery: String) : MoviesEvent
    data class ShowError(@StringRes val error: Int): MoviesEvent
}

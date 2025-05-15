package io.mohammedalaamorsi.movies.presentation.states

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import io.mohammedalaamorsi.movies.R
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.utils.GridItem


@Stable
data class MoviesState(
    val popularMovies: List<Movie> = emptyList(),
    val searchMovies: List<Movie> = emptyList(),
    val searchQuery: String = "",
    val currentMovies: List<GridItem> = emptyList(),
)


sealed class UiState {

    open val moviesState: MoviesState get() = MoviesState()

    @get:StringRes
    open val errorMessage: Int get() =  R.string.empty_string

    data object Loading : UiState()

    data class Result(val data: MoviesState) : UiState() {
        override val moviesState: MoviesState get() = data
    }

    data class Error(@StringRes val message: Int) : UiState() {
        @get:StringRes
        override val errorMessage: Int get() = message
    }
}

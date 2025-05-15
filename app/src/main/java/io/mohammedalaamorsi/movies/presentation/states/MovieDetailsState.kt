package io.mohammedalaamorsi.movies.presentation.states

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import io.mohammedalaamorsi.movies.R
import io.mohammedalaamorsi.movies.data.models.Cast
import io.mohammedalaamorsi.movies.data.models.Crew
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.data.models.MovieDetails

@Stable
data class MovieDetailsState(
    val isLoading: Boolean = true,
    @StringRes val error: Int = R.string.empty_string,

    val movieDetails: MovieDetails? = null,
    val isInWatchlist: Boolean = false,

    val similarMovies: List<Movie> = emptyList(),
    val isSimilarMoviesLoading: Boolean = true,
    @StringRes val similarMoviesError: Int = R.string.empty_string,

    val castMembers: Map<String, List<Cast>> = emptyMap(),
    val crewMembers: Map<String, List<Crew>> = emptyMap(),
    val isCastLoading: Boolean = true,
    @StringRes val castError: Int = R.string.empty_string,

    val topActors: List<Cast> = emptyList(),
    val topDirectors: List<Crew> = emptyList()
)

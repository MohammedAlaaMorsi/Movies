package io.mohammedalaamorsi.movies.presentation.states.events

import io.mohammedalaamorsi.movies.data.models.MovieDetails


sealed interface MovieDetailsEvent {
    data class GetMovieDetails(val movieId: Int) : MovieDetailsEvent
    data class AddToWatchList(val movieDetails: MovieDetails): MovieDetailsEvent
}

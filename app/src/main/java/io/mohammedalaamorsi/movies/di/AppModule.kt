package io.mohammedalaamorsi.movies.di

import android.app.Application
import io.mohammedalaamorsi.movies.presentation.movie_details.MovieDetailsViewModel
import io.mohammedalaamorsi.movies.presentation.movies_list.MoviesListViewModel
import io.mohammedalaamorsi.movies.utils.DispatchersProvider
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { DispatchersProvider() }
    viewModelOf(::MoviesListViewModel)
    viewModel { params ->
        MovieDetailsViewModel(
            movieId = params.get(),
            getMovieDetailsUseCase = get(),
            getSimilarMoviesUseCase = get(),
            getMovieCreditsUseCase = get(),
            addMovieToWatchlistUseCase = get(),
            removeMovieFromWatchlistUseCase = get()
        )
    }
    single { Application() }
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
}

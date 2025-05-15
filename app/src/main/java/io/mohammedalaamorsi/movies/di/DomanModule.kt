package io.mohammedalaamorsi.movies.di


import io.mohammedalaamorsi.movies.data.repository.MoviesRepositoryImp
import io.mohammedalaamorsi.movies.domain.MoviesRepository
import io.mohammedalaamorsi.movies.domain.usecase.GetMovieCreditsUseCase
import io.mohammedalaamorsi.movies.domain.usecase.GetMovieDetailsUseCase
import io.mohammedalaamorsi.movies.domain.usecase.GetPopularMoviesUseCase
import io.mohammedalaamorsi.movies.domain.usecase.GetSimilarMoviesUseCase
import io.mohammedalaamorsi.movies.domain.usecase.SearchMoviesUseCase
import io.mohammedalaamorsi.movies.domain.usecase.RemoveMovieFromWatchlistUseCase
import io.mohammedalaamorsi.movies.domain.usecase.AddMovieToWatchlistUseCase
import io.mohammedalaamorsi.movies.domain.usecase.GetWatchlistMoviesUseCase

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val domainModule = module {
    singleOf(::MoviesRepositoryImp) { bind<MoviesRepository>() }
    factoryOf(::GetPopularMoviesUseCase)
    factoryOf(::GetSimilarMoviesUseCase)
    factoryOf(::GetMovieCreditsUseCase)
    factoryOf(::GetMovieDetailsUseCase)
    factoryOf(::SearchMoviesUseCase)
    factoryOf(::AddMovieToWatchlistUseCase)
    factoryOf(::RemoveMovieFromWatchlistUseCase)
    factoryOf(::GetWatchlistMoviesUseCase)


}

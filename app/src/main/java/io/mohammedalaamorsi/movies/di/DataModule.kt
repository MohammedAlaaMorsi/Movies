package io.mohammedalaamorsi.movies.di

import io.mohammedalaamorsi.movies.data.locale.LocaleMoviesDataSource
import io.mohammedalaamorsi.movies.data.locale.MovieDao
import io.mohammedalaamorsi.movies.data.locale.MoviesDatabase
import io.mohammedalaamorsi.movies.data.remote.AndroidClientProvider
import io.mohammedalaamorsi.movies.data.remote.HttpClientProvider
import io.mohammedalaamorsi.movies.data.remote.KtorHttpClientService
import io.mohammedalaamorsi.movies.data.remote.MoviesUrlProvider
import io.mohammedalaamorsi.movies.data.remote.RemoteMoviesDataSource
import io.mohammedalaamorsi.movies.data.remote.UrlsProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val dataModule = module {
    single { MoviesDatabase.getInstance(androidContext()) }

    single<MovieDao> {
        val appDatabase = get<MoviesDatabase>()
        appDatabase.movieDao()
    }
    singleOf(::RemoteMoviesDataSource)
    singleOf(::LocaleMoviesDataSource)
    factory<HttpClientProvider> { AndroidClientProvider() }
    singleOf(::KtorHttpClientService)
    single<UrlsProvider> { MoviesUrlProvider() }
}

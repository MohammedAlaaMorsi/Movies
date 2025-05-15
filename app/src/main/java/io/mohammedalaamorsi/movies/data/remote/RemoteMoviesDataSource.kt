package io.mohammedalaamorsi.movies.data.remote

import io.mohammedalaamorsi.movies.data.models.Credit
import io.mohammedalaamorsi.movies.data.models.MovieDetails
import io.mohammedalaamorsi.movies.data.models.PopularMoviesResponse

class RemoteMoviesDataSource(
    private val ktorHttpClientService: KtorHttpClientService,
    private val urlsProvider: UrlsProvider,
) {
    suspend fun searchMovies(
        searchQuery: String,
        includeAdult: Boolean = false,
        language: String = "en-US",
        page: Int = 1
    ): PopularMoviesResponse {
        return ktorHttpClientService.loadRemoteData(
            apiPath = urlsProvider.searchMovies(searchQuery, includeAdult, language, page),
            serializer = PopularMoviesResponse.serializer()
        ).getOrThrow()
    }

    suspend fun getPopularMovies(
        language: String = "en-US",
        page: Int = 1
    ): PopularMoviesResponse {
        return ktorHttpClientService.loadRemoteData(
            apiPath = urlsProvider.getPopularMovies(language, page),
            serializer = PopularMoviesResponse.serializer()
        ).getOrThrow()
    }

    suspend fun getMovieDetails(
        movieId: Int,
        language: String = "en-US"
    ): MovieDetails {
        return ktorHttpClientService.loadRemoteData(
            apiPath = urlsProvider.getMovieDetails(movieId, language),
            serializer = MovieDetails.serializer()
        ).getOrThrow()
    }

    suspend fun getSimilarMovies(
        movieId: Int,
        language: String = "en-US",
        page: Int = 1
    ): PopularMoviesResponse {
        return ktorHttpClientService.loadRemoteData(
            apiPath = urlsProvider.getSimilarMovies(movieId, language, page),
            serializer =PopularMoviesResponse.serializer()
        ).getOrThrow()
    }

    suspend fun getMovieCredits(
        movieId: Int,
        language: String = "en-US"
    ): Credit {
        return ktorHttpClientService.loadRemoteData(
            apiPath = urlsProvider.getMovieCredits(movieId, language),
            serializer = Credit.serializer()
        ).getOrThrow()
    }
}

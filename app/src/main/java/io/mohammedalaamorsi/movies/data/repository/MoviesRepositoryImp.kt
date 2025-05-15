package io.mohammedalaamorsi.movies.data.repository

import io.mohammedalaamorsi.movies.data.locale.LocaleMoviesDataSource
import io.mohammedalaamorsi.movies.data.models.Credit
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.data.models.MovieDetails
import io.mohammedalaamorsi.movies.data.models.MovieEntity
import io.mohammedalaamorsi.movies.data.remote.RemoteMoviesDataSource
import io.mohammedalaamorsi.movies.domain.MoviesRepository
import io.mohammedalaamorsi.movies.utils.toMovie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MoviesRepositoryImp(
    private val remoteMoviesDataSource: RemoteMoviesDataSource,
    private val localeMoviesDataSource: LocaleMoviesDataSource
) : MoviesRepository {
    override suspend fun getWatchlistMovies(): Flow<List<Movie>> {
        return localeMoviesDataSource.observeWatchlistMovies().map { movieEntities ->
            movieEntities.map { it.toMovie() }
        }
    }


    override suspend fun addMovieToWatchlist(movieEntity: MovieEntity): Flow<Boolean> {
        return flow {
            localeMoviesDataSource.addMovieToWatchList(movieEntity)
            emit(true)
        }
    }

    override suspend fun removeMovieFromWatchlist(movieId: Int): Flow<Boolean> {
        return flow {
            localeMoviesDataSource.removeMovieFromWatchlist(movieId)
            emit(true)
        }
    }


    override suspend fun searchMovies(
        searchQuery: String,
        includeAdult: Boolean,
        language: String,
        page: Int
    ): Flow<List<Movie>> {
        return flow {
            val moviesList: List<Movie> = remoteMoviesDataSource.searchMovies(
                searchQuery,
                includeAdult,
                language,
                page
            ).results

            val updatedMovies = moviesList.map { movie ->
                movie.copy(isInWatchlist = localeMoviesDataSource.isMovieInWatchList(movie.id))
            }
            emit(updatedMovies)
        }
    }


    override suspend fun getPopularMovies(): Flow<List<Movie>> {
        return flow {
            val moviesList = remoteMoviesDataSource.getPopularMovies().results

            val updatedMovies = moviesList.map { movie ->
                movie.copy(isInWatchlist = localeMoviesDataSource.isMovieInWatchList(movie.id))
            }
            emit(updatedMovies)
        }
    }

    override suspend fun getMovieDetails(movieId: Int, language: String): Flow<MovieDetails> {
        return flow {
            val movieDetails = remoteMoviesDataSource.getMovieDetails(movieId, language)
            // Check if this movie is in the watchlist
            val isInWatchlist = localeMoviesDataSource.isMovieInWatchList(movieId)
            // Create a copy with updated watchlist status
            val updatedMovieDetails = movieDetails.copy(isInWatchlist = isInWatchlist)
            emit(updatedMovieDetails)
        }
    }

    override suspend fun getSimilarMovies(
        movieId: Int,
        language: String,
        page: Int
    ): Flow<List<Movie>> {
        return flow {
            val moviesList =
                remoteMoviesDataSource.getSimilarMovies(movieId, language, page).results

            val updatedMovies = moviesList.map { movie ->
                movie.copy(isInWatchlist = localeMoviesDataSource.isMovieInWatchList(movie.id))
            }
            emit(updatedMovies)
        }
    }

    override suspend fun getMovieCredits(movieId: Int, language: String): Flow<Credit> {
        return flow {
            val credits = remoteMoviesDataSource.getMovieCredits(movieId, language)
            emit(credits)
        }
    }
}

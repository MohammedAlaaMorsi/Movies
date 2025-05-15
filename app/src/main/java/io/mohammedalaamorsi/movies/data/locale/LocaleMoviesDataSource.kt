package io.mohammedalaamorsi.movies.data.locale

import io.mohammedalaamorsi.movies.data.models.MovieEntity
import kotlinx.coroutines.flow.Flow

class LocaleMoviesDataSource(
    private val movieDao: MovieDao
) {
    suspend fun isMovieInWatchList(movieId: Int): Boolean {
        return movieDao.getMovieById(movieId) != null
    }

    suspend fun addMovieToWatchList(movie: MovieEntity) {
        return movieDao.insertMovie(movie)
    }

    fun observeWatchlistMovies(): Flow<List<MovieEntity>> {
        return movieDao.observeAllMovies()
    }

    suspend fun removeMovieFromWatchlist(movieId: Int) {
        return movieDao.deleteMovie(movieId)
    }
}

package io.mohammedalaamorsi.movies.domain

import io.mohammedalaamorsi.movies.data.models.Credit
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.data.models.MovieDetails
import io.mohammedalaamorsi.movies.data.models.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getWatchlistMovies(): Flow<List<Movie>>
    suspend fun addMovieToWatchlist(movieEntity: MovieEntity): Flow<Boolean>
    suspend fun removeMovieFromWatchlist(movieId: Int): Flow<Boolean>
    suspend fun searchMovies(searchQuery:String,includeAdult:Boolean=false,language: String="en-US",page: Int=1): Flow<List<Movie>>
    suspend fun getPopularMovies(): Flow<List<Movie>>
    suspend fun getMovieDetails(movieId: Int, language: String="en-US"): Flow<MovieDetails>
    suspend fun getSimilarMovies(movieId: Int,language: String="en-US", page: Int=1): Flow<List<Movie>>
    suspend fun getMovieCredits(movieId: Int,language: String="en-US"): Flow<Credit>
}

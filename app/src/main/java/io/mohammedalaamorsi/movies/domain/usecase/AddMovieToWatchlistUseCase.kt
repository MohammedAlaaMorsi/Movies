package io.mohammedalaamorsi.movies.domain.usecase

import io.mohammedalaamorsi.movies.data.models.MovieEntity
import io.mohammedalaamorsi.movies.domain.MoviesRepository
import io.mohammedalaamorsi.movies.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AddMovieToWatchlistUseCase(
    private val moviesRepository: MoviesRepository,
    private val dispatchersProvider: DispatchersProvider,
) {
    suspend operator fun invoke(movieEntity: MovieEntity): Flow<Boolean> {
        return moviesRepository
            .addMovieToWatchlist(movieEntity = movieEntity)
            .flowOn(dispatchersProvider.io)
    }
}

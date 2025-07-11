package io.mohammedalaamorsi.movies.domain.usecase

import io.mohammedalaamorsi.movies.domain.MoviesRepository
import io.mohammedalaamorsi.movies.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RemoveMovieFromWatchlistUseCase(
    private val moviesRepository: MoviesRepository,
    private val dispatchersProvider: DispatchersProvider,
) {
    suspend operator fun invoke(movieId: Int): Flow<Boolean> {
        return moviesRepository
            .removeMovieFromWatchlist(movieId = movieId)
            .flowOn(dispatchersProvider.io)
    }
}

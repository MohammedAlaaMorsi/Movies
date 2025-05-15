package io.mohammedalaamorsi.movies.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mohammedalaamorsi.movies.domain.MoviesRepository
import io.mohammedalaamorsi.movies.utils.MainDispatcherRule
import io.mohammedalaamorsi.movies.utils.TestDispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RemoveMovieFromWatchlistUseCaseTest : BaseUseCaseTest() {
    
    // Subject under test
    private lateinit var useCase: RemoveMovieFromWatchlistUseCase
    
    // Dependencies
    private val repository: MoviesRepository = mockk(relaxed = true)
    
    @Before
    fun setup() {
        useCase = RemoveMovieFromWatchlistUseCase(repository, testDispatchersProvider)
    }
    
    @Test
    fun `invoke should remove movie from watchlist and return success`() = runTest {
        // Given
        val movieId = 123
        coEvery { repository.removeMovieFromWatchlist(movieId) } returns flowOf(true)
        
        // When
        val result = useCase(movieId)
        
        // Then
        result.test {
            val success = awaitItem()
            assertThat(success).isTrue()
            awaitComplete()
        }
        
        coVerify(exactly = 1) { repository.removeMovieFromWatchlist(movieId) }
    }
}

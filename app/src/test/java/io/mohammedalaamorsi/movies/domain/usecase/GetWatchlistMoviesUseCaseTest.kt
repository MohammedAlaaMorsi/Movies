package io.mohammedalaamorsi.movies.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mohammedalaamorsi.movies.domain.MoviesRepository
import io.mohammedalaamorsi.movies.utils.MainDispatcherRule
import io.mohammedalaamorsi.movies.utils.TestData
import io.mohammedalaamorsi.movies.utils.TestDispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetWatchlistMoviesUseCaseTest : BaseUseCaseTest() {
    
    // Subject under test
    private lateinit var useCase: GetWatchlistMoviesUseCase
    
    // Dependencies
    private val repository: MoviesRepository = mockk(relaxed = true)
    
    @Before
    fun setup() {
        useCase = GetWatchlistMoviesUseCase(repository, testDispatchersProvider)
    }
    
    @Test
    fun `invoke should return watchlist movies from repository`() = runTest {
        // Given
        val watchlistMovies = TestData.testMoviesList
        coEvery { repository.getWatchlistMovies() } returns flowOf(watchlistMovies)
        
        // When
        val result = useCase()
        
        // Then
        result.test {
            val movies = awaitItem()
            assertThat(movies).isEqualTo(watchlistMovies)
            assertThat(movies).hasSize(3)
            awaitComplete()
        }
        
        coVerify(exactly = 1) { repository.getWatchlistMovies() }
    }
}

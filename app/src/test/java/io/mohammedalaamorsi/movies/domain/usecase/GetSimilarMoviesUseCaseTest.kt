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

class GetSimilarMoviesUseCaseTest : BaseUseCaseTest() {
    
    // Subject under test
    private lateinit var useCase: GetSimilarMoviesUseCase
    
    // Dependencies
    private val repository: MoviesRepository = mockk(relaxed = true)
    
    @Before
    fun setup() {
        useCase = GetSimilarMoviesUseCase(repository, testDispatchersProvider)
    }
    
    @Test
    fun `invoke should return similar movies from repository`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"  // Default language parameter
        val page = 1  // Default page parameter
        val similarMovies = TestData.testMoviesList
        coEvery { repository.getSimilarMovies(movieId, language, page) } returns flowOf(similarMovies)
        
        // When
        val result = useCase(movieId)
        
        // Then
        result.test {
            val movies = awaitItem()
            assertThat(movies).isEqualTo(similarMovies)
            assertThat(movies).hasSize(3)
            awaitComplete()
        }
        
        coVerify(exactly = 1) { repository.getSimilarMovies(movieId, language, page) }
    }
}

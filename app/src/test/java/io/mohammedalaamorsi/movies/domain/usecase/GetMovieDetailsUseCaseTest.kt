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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetMovieDetailsUseCaseTest : BaseUseCaseTest() {
    
    // Subject under test
    private lateinit var useCase: GetMovieDetailsUseCase
    
    // Dependencies
    private val repository: MoviesRepository = mockk(relaxed = true)
    
    @Before
    fun setup() {
        useCase = GetMovieDetailsUseCase(repository, testDispatchersProvider)
    }
    
    @Test
    fun `invoke should return movie details from repository`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"  // Default language parameter
        val movieDetails = TestData.testMovieDetails
        coEvery { repository.getMovieDetails(movieId, language) } returns flowOf(movieDetails)
        
        // When
        val result = useCase(movieId)
        
        // Then
        result.test {
            val details = awaitItem()
            assertThat(details).isEqualTo(movieDetails)
            assertThat(details.id).isEqualTo(movieId)
            awaitComplete()
        }
        
        coVerify(exactly = 1) { repository.getMovieDetails(movieId, language) }
    }
}

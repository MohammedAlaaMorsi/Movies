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

class GetMovieCreditsUseCaseTest : BaseUseCaseTest() {
    
    // Subject under test
    private lateinit var useCase: GetMovieCreditsUseCase
    
    // Dependencies
    private val repository: MoviesRepository = mockk(relaxed = true)
    
    @Before
    fun setup() {
        useCase = GetMovieCreditsUseCase(repository, testDispatchersProvider)
    }
    
    @Test
    fun `invoke should return movie credits from repository`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"  // Default language parameter
        val credits = TestData.testCredit
        coEvery { repository.getMovieCredits(movieId, language) } returns flowOf(credits)
        
        // When
        val result = useCase(movieId)
        
        // Then
        result.test {
            val receivedCredits = awaitItem()
            assertThat(receivedCredits).isEqualTo(credits)
            assertThat(receivedCredits.cast).hasSize(1)
            assertThat(receivedCredits.crew).hasSize(1)
            awaitComplete()
        }
        
        coVerify(exactly = 1) { repository.getMovieCredits(movieId, language) }
    }
}

package io.mohammedalaamorsi.movies.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mohammedalaamorsi.movies.domain.MoviesRepository
import io.mohammedalaamorsi.movies.utils.TestData
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddMovieToWatchlistUseCaseTest : BaseUseCaseTest() {
    
    // Subject under test
    private lateinit var useCase: AddMovieToWatchlistUseCase
    
    // Dependencies
    private val repository: MoviesRepository = mockk(relaxed = true)
    
    @Before
    fun setup() {
        useCase = AddMovieToWatchlistUseCase(repository, testDispatchersProvider)
    }
    
    @Test
    fun `invoke should add movie to watchlist and return success`() = runTest {
        // Given
        val movieEntity = TestData.testMovieEntity
        coEvery { repository.addMovieToWatchlist(movieEntity) } returns flowOf(true)
        
        // When
        val result = useCase(movieEntity)
        
        // Then
        result.test {
            val success = awaitItem()
            assertThat(success).isTrue()
            awaitComplete()
        }
        
        coVerify(exactly = 1) { repository.addMovieToWatchlist(movieEntity) }
    }
}

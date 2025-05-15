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

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMoviesUseCaseTest {
    
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    
    // Subject under test
    private lateinit var useCase: SearchMoviesUseCase
    
    // Dependencies
    private val repository: MoviesRepository = mockk()
    private val dispatchersProvider = TestDispatchersProvider()
    
    @Before
    fun setup() {
        useCase = SearchMoviesUseCase(repository, dispatchersProvider)
    }
    
    @Test
    fun `invoke should return search results from repository`() = runTest {
        // Given
        val query = "test"
        val includeAdult = false
        val language = "en-US"
        val page = 1
        val searchResults = TestData.testMoviesList
        
        coEvery { repository.searchMovies(query) } returns flowOf(searchResults)
        
        // When
        val result = useCase(query)
        
        // Then
        result.test {
            val movies = awaitItem()
            assertThat(movies).isEqualTo(searchResults)
            assertThat(movies).hasSize(3)
            awaitComplete()
        }
        
        coVerify(exactly = 1) { repository.searchMovies(query, includeAdult, language, page) }
    }
    
    @Test
    fun `invoke should use default parameters if not provided`() = runTest {
        // Given
        val query = "test"
        val defaultIncludeAdult = false
        val defaultLanguage = "en-US"
        val defaultPage = 1
        val searchResults = TestData.testMoviesList
        
        coEvery { repository.searchMovies(query, defaultIncludeAdult, defaultLanguage, defaultPage) } returns flowOf(searchResults)
        
        // When
        val result = useCase(query)
        
        // Then
        result.test {
            val movies = awaitItem()
            assertThat(movies).isEqualTo(searchResults)
            awaitComplete()
        }
        
        coVerify(exactly = 1) { repository.searchMovies(query, defaultIncludeAdult, defaultLanguage, defaultPage) }
    }
}

package io.mohammedalaamorsi.movies.data.locale

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mohammedalaamorsi.movies.data.BaseDataLayerTest
import io.mohammedalaamorsi.movies.utils.TestData
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LocaleMoviesDataSourceTest : BaseDataLayerTest() {
    
    // Subject under test
    private lateinit var localeDataSource: LocaleMoviesDataSource
    
    // Dependencies
    private val movieDao: MovieDao = mockk(relaxed = true)
    
    @Before
    fun setup() {
        localeDataSource = LocaleMoviesDataSource(movieDao)
    }
    
    @Test
    fun `isMovieInWatchList should return true if movie exists in database`() = runTest {
        // Given
        val movieId = 123
        coEvery { movieDao.getMovieById(movieId) } returns TestData.testMovieEntity
        
        // When
        val result = localeDataSource.isMovieInWatchList(movieId)
        
        // Then
        assertThat(result).isTrue()
        coVerify(exactly = 1) { movieDao.getMovieById(movieId) }
    }
    
    @Test
    fun `isMovieInWatchList should return false if movie does not exist in database`() = runTest {
        // Given
        val movieId = 123
        coEvery { movieDao.getMovieById(movieId) } returns null
        
        // When
        val result = localeDataSource.isMovieInWatchList(movieId)
        
        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 1) { movieDao.getMovieById(movieId) }
    }
    
    @Test
    fun `addMovieToWatchList should add movie to database`() = runTest {
        // Given
        val movie = TestData.testMovieEntity
        coEvery { movieDao.insertMovie(movie) } returns Unit
        
        // When
        localeDataSource.addMovieToWatchList(movie)
        
        // Then
        coVerify(exactly = 1) { movieDao.insertMovie(movie) }
    }
    
    @Test
    fun `observeWatchlistMovies should return flow of movies from database`() = runTest {
        // Given
        val movies = TestData.testMovieEntitiesList
        coEvery { movieDao.observeAllMovies() } returns flowOf(movies)
        
        // When
        val result = localeDataSource.observeWatchlistMovies()
        
        // Then
        result.test {
            val receivedMovies = awaitItem()
            assertThat(receivedMovies).isEqualTo(movies)
            assertThat(receivedMovies).hasSize(3)
            cancelAndConsumeRemainingEvents()
        }
        
        coVerify(exactly = 1) { movieDao.observeAllMovies() }
    }
    
    @Test
    fun `removeMovieFromWatchlist should delete movie from database`() = runTest {
        // Given
        val movieId = 123
        coEvery { movieDao.deleteMovie(movieId) } returns Unit
        
        // When
        localeDataSource.removeMovieFromWatchlist(movieId)
        
        // Then
        coVerify(exactly = 1) { movieDao.deleteMovie(movieId) }
    }
}

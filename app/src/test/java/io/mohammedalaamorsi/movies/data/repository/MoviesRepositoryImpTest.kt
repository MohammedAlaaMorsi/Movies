package io.mohammedalaamorsi.movies.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mohammedalaamorsi.movies.data.BaseDataLayerTest
import io.mohammedalaamorsi.movies.data.locale.LocaleMoviesDataSource
import io.mohammedalaamorsi.movies.data.models.MovieEntity
import io.mohammedalaamorsi.movies.data.remote.RemoteMoviesDataSource
import io.mohammedalaamorsi.movies.utils.TestData
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MoviesRepositoryImpTest : BaseDataLayerTest() {
    
    // Subject under test
    private lateinit var repository: MoviesRepositoryImp
    
    // Dependencies
    private val remoteDataSource: RemoteMoviesDataSource = mockk(relaxed = true)
    private val localeDataSource: LocaleMoviesDataSource = mockk(relaxed = true)
    
    @Before
    fun setup() {
        repository = MoviesRepositoryImp(remoteDataSource, localeDataSource)
    }
    
    @Test
    fun `getWatchlistMovies should return movies from local source`() = runTest {
        // Given
        val movieEntities = TestData.testMovieEntitiesList
        coEvery { localeDataSource.observeWatchlistMovies() } returns flowOf(movieEntities)
        
        // When
        val result = repository.getWatchlistMovies()
        
        // Then
        result.test {
            val movies = awaitItem()
            assertThat(movies).hasSize(3)
            assertThat(movies[0].id).isEqualTo(123)
            assertThat(movies[1].id).isEqualTo(124)
            assertThat(movies[2].id).isEqualTo(125)
            awaitComplete()
        }
        
        coVerify(exactly = 1) { localeDataSource.observeWatchlistMovies() }
    }
    
    @Test
    fun `addMovieToWatchlist should add movie to database and return true`() = runTest {
        // Given
        val movieEntity = TestData.testMovieEntity
        coEvery { localeDataSource.addMovieToWatchList(movieEntity) } returns Unit
        
        // When
        val result = repository.addMovieToWatchlist(movieEntity)
        
        // Then
        result.test {
            val success = awaitItem()
            assertThat(success).isTrue()
            awaitComplete()
        }
        
        coVerify(exactly = 1) { localeDataSource.addMovieToWatchList(movieEntity) }
    }
    
    @Test
    fun `removeMovieFromWatchlist should remove movie from database and return true`() = runTest {
        // Given
        val movieId = 123
        coEvery { localeDataSource.removeMovieFromWatchlist(movieId) } returns Unit
        
        // When
        val result = repository.removeMovieFromWatchlist(movieId)
        
        // Then
        result.test {
            val success = awaitItem()
            assertThat(success).isTrue()
            awaitComplete()
        }
        
        coVerify(exactly = 1) { localeDataSource.removeMovieFromWatchlist(movieId) }
    }
    
    @Test
    fun `getPopularMovies should fetch movies and update watchlist status`() = runTest {
        // Given
        val popularMoviesResponse = TestData.testPopularMoviesResponse
        coEvery { remoteDataSource.getPopularMovies() } returns popularMoviesResponse
        coEvery { localeDataSource.isMovieInWatchList(123) } returns false
        coEvery { localeDataSource.isMovieInWatchList(124) } returns true
        coEvery { localeDataSource.isMovieInWatchList(125) } returns false
        
        // When
        val result = repository.getPopularMovies()
        
        // Then
        result.test {
            val movies = awaitItem()
            assertThat(movies).hasSize(3)
            assertThat(movies[0].isInWatchlist).isFalse()
            assertThat(movies[1].isInWatchlist).isTrue()
            assertThat(movies[2].isInWatchlist).isFalse()
            awaitComplete()
        }
        
        coVerify(exactly = 1) { remoteDataSource.getPopularMovies() }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(123) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(124) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(125) }
    }
    
    @Test
    fun `getMovieDetails should fetch movie details and update watchlist status`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"
        val movieDetails = TestData.testMovieDetails
        coEvery { remoteDataSource.getMovieDetails(movieId, language) } returns movieDetails
        coEvery { localeDataSource.isMovieInWatchList(movieId) } returns true
        
        // When
        val result = repository.getMovieDetails(movieId, language)
        
        // Then
        result.test {
            val details = awaitItem()
            assertThat(details.id).isEqualTo(movieId)
            assertThat(details.isInWatchlist).isTrue() // This should be updated from the database
            awaitComplete()
        }
        
        coVerify(exactly = 1) { remoteDataSource.getMovieDetails(movieId, language) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(movieId) }
    }
    
    @Test
    fun `getSimilarMovies should fetch similar movies and update watchlist status`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"
        val page = 1
        val similarMoviesResponse = TestData.testPopularMoviesResponse
        coEvery { remoteDataSource.getSimilarMovies(movieId, language, page) } returns similarMoviesResponse
        coEvery { localeDataSource.isMovieInWatchList(123) } returns false
        coEvery { localeDataSource.isMovieInWatchList(124) } returns false
        coEvery { localeDataSource.isMovieInWatchList(125) } returns true
        
        // When
        val result = repository.getSimilarMovies(movieId, language, page)
        
        // Then
        result.test {
            val movies = awaitItem()
            assertThat(movies).hasSize(3)
            assertThat(movies[0].isInWatchlist).isFalse()
            assertThat(movies[1].isInWatchlist).isFalse()
            assertThat(movies[2].isInWatchlist).isTrue()
            awaitComplete()
        }
        
        coVerify(exactly = 1) { remoteDataSource.getSimilarMovies(movieId, language, page) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(123) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(124) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(125) }
    }
    
    @Test
    fun `getMovieCredits should fetch movie credits`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"
        val credit = TestData.testCredit
        coEvery { remoteDataSource.getMovieCredits(movieId, language) } returns credit
        
        // When
        val result = repository.getMovieCredits(movieId, language)
        
        // Then
        result.test {
            val credits = awaitItem()
            assertThat(credits.id).isEqualTo(movieId)
            assertThat(credits.cast).hasSize(1)
            assertThat(credits.crew).hasSize(1)
            awaitComplete()
        }
        
        coVerify(exactly = 1) { remoteDataSource.getMovieCredits(movieId, language) }
    }
    
    @Test
    fun `searchMovies should search movies and update watchlist status`() = runTest {
        // Given
        val query = "test"
        val includeAdult = false
        val language = "en-US"
        val page = 1
        val searchResponse = TestData.testPopularMoviesResponse
        coEvery { remoteDataSource.searchMovies(query, includeAdult, language, page) } returns searchResponse
        coEvery { localeDataSource.isMovieInWatchList(123) } returns true
        coEvery { localeDataSource.isMovieInWatchList(124) } returns false
        coEvery { localeDataSource.isMovieInWatchList(125) } returns false
        
        // When
        val result = repository.searchMovies(query, includeAdult, language, page)
        
        // Then
        result.test {
            val movies = awaitItem()
            assertThat(movies).hasSize(3)
            assertThat(movies[0].isInWatchlist).isTrue()
            assertThat(movies[1].isInWatchlist).isFalse()
            assertThat(movies[2].isInWatchlist).isFalse()
            awaitComplete()
        }
        
        coVerify(exactly = 1) { remoteDataSource.searchMovies(query, includeAdult, language, page) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(123) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(124) }
        coVerify(exactly = 1) { localeDataSource.isMovieInWatchList(125) }
    }
}

package io.mohammedalaamorsi.movies.data.remote

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mohammedalaamorsi.movies.data.BaseDataLayerTest
import io.mohammedalaamorsi.movies.data.models.Credit
import io.mohammedalaamorsi.movies.data.models.MovieDetails
import io.mohammedalaamorsi.movies.data.models.PopularMoviesResponse
import io.mohammedalaamorsi.movies.utils.TestData
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoteMoviesDataSourceTest : BaseDataLayerTest() {
    
    // Subject under test
    private lateinit var remoteDataSource: RemoteMoviesDataSource
    
    // Dependencies
    private val ktorHttpClientService: KtorHttpClientService = mockk(relaxed = true)
    private val urlsProvider: UrlsProvider = mockk(relaxed = true)
    
    @Before
    fun setup() {
        remoteDataSource = RemoteMoviesDataSource(ktorHttpClientService, urlsProvider)
    }
    
    @Test
    fun `searchMovies should return popular movies response`() = runTest {
        // Given
        val query = "test"
        val includeAdult = false
        val language = "en-US"
        val page = 1
        val apiPath = "search/movie?query=test&include_adult=false&language=en-US&page=1"
        val expectedResponse = TestData.testPopularMoviesResponse
        
        coEvery { urlsProvider.searchMovies(query, includeAdult, language, page) } returns apiPath
        coEvery { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = PopularMoviesResponse.serializer()
            ) 
        } returns Result.success(expectedResponse)
        
        // When
        val result = remoteDataSource.searchMovies(query, includeAdult, language, page)
        
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { urlsProvider.searchMovies(query, includeAdult, language, page) }
        coVerify(exactly = 1) { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = PopularMoviesResponse.serializer()
            )
        }
    }
    
    @Test
    fun `getPopularMovies should return popular movies response`() = runTest {
        // Given
        val language = "en-US"
        val page = 1
        val apiPath = "movie/popular?language=en-US&page=1"
        val expectedResponse = TestData.testPopularMoviesResponse
        
        coEvery { urlsProvider.getPopularMovies(language, page) } returns apiPath
        coEvery { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = PopularMoviesResponse.serializer()
            ) 
        } returns Result.success(expectedResponse)
        
        // When
        val result = remoteDataSource.getPopularMovies(language, page)
        
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { urlsProvider.getPopularMovies(language, page) }
        coVerify(exactly = 1) { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = PopularMoviesResponse.serializer()
            )
        }
    }
    
    @Test
    fun `getMovieDetails should return movie details`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"
        val apiPath = "movie/123?language=en-US"
        val expectedResponse = TestData.testMovieDetails
        
        coEvery { urlsProvider.getMovieDetails(movieId, language) } returns apiPath
        coEvery { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = MovieDetails.serializer()
            ) 
        } returns Result.success(expectedResponse)
        
        // When
        val result = remoteDataSource.getMovieDetails(movieId, language)
        
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { urlsProvider.getMovieDetails(movieId, language) }
        coVerify(exactly = 1) { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = MovieDetails.serializer()
            )
        }
    }
    
    @Test
    fun `getSimilarMovies should return similar movies response`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"
        val page = 1
        val apiPath = "movie/123/similar?language=en-US&page=1"
        val expectedResponse = TestData.testPopularMoviesResponse
        
        coEvery { urlsProvider.getSimilarMovies(movieId, language, page) } returns apiPath
        coEvery { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = PopularMoviesResponse.serializer()
            ) 
        } returns Result.success(expectedResponse)
        
        // When
        val result = remoteDataSource.getSimilarMovies(movieId, language, page)
        
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { urlsProvider.getSimilarMovies(movieId, language, page) }
        coVerify(exactly = 1) { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = PopularMoviesResponse.serializer()
            )
        }
    }
    
    @Test
    fun `getMovieCredits should return movie credits`() = runTest {
        // Given
        val movieId = 123
        val language = "en-US"
        val apiPath = "movie/123/credits?language=en-US"
        val expectedResponse = TestData.testCredit
        
        coEvery { urlsProvider.getMovieCredits(movieId, language) } returns apiPath
        coEvery { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = Credit.serializer()
            ) 
        } returns Result.success(expectedResponse)
        
        // When
        val result = remoteDataSource.getMovieCredits(movieId, language)
        
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { urlsProvider.getMovieCredits(movieId, language) }
        coVerify(exactly = 1) { 
            ktorHttpClientService.loadRemoteData(
                apiPath = apiPath, 
                serializer = Credit.serializer()
            )
        }
    }
}

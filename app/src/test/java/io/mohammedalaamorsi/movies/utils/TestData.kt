package io.mohammedalaamorsi.movies.utils

import io.mohammedalaamorsi.movies.data.models.Cast
import io.mohammedalaamorsi.movies.data.models.Credit
import io.mohammedalaamorsi.movies.data.models.Crew
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.data.models.MovieDetails
import io.mohammedalaamorsi.movies.data.models.MovieEntity
import io.mohammedalaamorsi.movies.data.models.PopularMoviesResponse

/**
 * Test data for unit testing
 */
object TestData {
    // Movie data
    val testMovie = Movie(
        adult = false,
        backdropPath = "/backdrop_path.jpg",
        id = 123,
        title = "Test Movie",
        originalLanguage = "en",
        originalTitle = "Test Movie Original",
        overview = "This is a test movie overview",
        posterPath = "/poster_path.jpg",
        genreIds = listOf(28, 12),
        popularity = 8.5,
        releaseDate = "2025-05-15",
        video = false,
        voteAverage = 7.9,
        voteCount = 1000,
        isInWatchlist = false
    )

    val testMovieEntity = MovieEntity(
        id = 123,
        title = "Test Movie",
        overview = "This is a test movie overview",
        posterPath = "/poster_path.jpg",
        releaseDate = "2025-05-15",
        voteAverage = 7.9,
        voteCount = 1000,
       originalLanguage = "en",
        originalTitle = "Test Movie Original",
        popularity = 6.7

    )
    
    val testMoviesList = listOf(
        testMovie,
        testMovie.copy(id = 124, title = "Test Movie 2"),
        testMovie.copy(id = 125, title = "Test Movie 3", isInWatchlist = true)
    )
    
    val testMovieEntitiesList = listOf(
        testMovieEntity,
        testMovieEntity.copy(id = 124, title = "Test Movie 2"),
        testMovieEntity.copy(id = 125, title = "Test Movie 3")
    )
    
    // Movie Details data
    val testMovieDetails = MovieDetails(
        adult = false,
        backdropPath = "/backdrop_path.jpg",
        budget = 150000000,
        genres = emptyList(),
        homepage = "https://example.com",
        id = 123,
        imdbId = "tt1234567",
        originalLanguage = "en",
        originalTitle = "Test Movie Original",
        overview = "This is a test movie overview",
        popularity = 8.5,
        posterPath = "/poster_path.jpg",
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        releaseDate = "2025-05-15",
        revenue = 300000000,
        runtime = 120,
        spokenLanguages = emptyList(),
        status = "Released",
        tagline = "A tagline for testing",
        title = "Test Movie",
        video = false,
        voteAverage = 7.9,
        voteCount = 1000,
        originCountry = listOf("US"),
        isInWatchlist = false
    )
    
    // Credits data
    val testCredit = Credit(
        id = 123,
        cast = listOf(
            Cast(
                adult = false,
                gender = 1,
                id = 456,
                knownForDepartment = "Acting",
                name = "Test Actor",
                originalName = "Test Actor",
                popularity = 7.5,
                profilePath = "/profile_path.jpg",
                castId = 1,
                character = "Test Character",
                creditId = "credit123",
                order = 0
            )
        ),
        crew = listOf(
            Crew(
                adult = false,
                gender = 2,
                id = 789,
                knownForDepartment = "Directing",
                name = "Test Director",
                originalName = "Test Director",
                popularity = 6.5,
                profilePath = "/director_profile.jpg",
                creditId = "credit456",
                department = "Directing",
                job = "Director"
            )
        )
    )
    
    // API Responses
    val testPopularMoviesResponse = PopularMoviesResponse(
        page = 1,
        results = testMoviesList,
        totalPages = 10,
        totalResults = 100
    )
}

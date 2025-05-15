package io.mohammedalaamorsi.movies.utils

import io.mohammedalaamorsi.movies.BuildConfig
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.data.models.MovieDetails
import io.mohammedalaamorsi.movies.data.models.MovieEntity
import java.text.NumberFormat
import java.util.Locale


fun String?.toImageUrl(): String {
    return "${BuildConfig.IMAGE_BASE_URL}$this"
}

fun Int.formatCurrency(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(this)
}

fun MovieDetails.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = posterPath,
        voteAverage = voteAverage,
        voteCount = voteCount,
        originalLanguage = originalLanguage,
        popularity = popularity
    )
}

fun Movie.fromMovie(): MovieEntity {
    return MovieEntity(
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun Movie.toMovieDetails(): MovieDetails {
    return MovieDetails(
        adult = false, // Default value
        backdropPath = null,
        belongsToCollection = null,
        budget = 0,
        genres = emptyList(),
        homepage = "",
        id = id,
        imdbId = null,
        originCountry = emptyList(),
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        productionCompanies = emptyList(),
        productionCountries = emptyList(),
        releaseDate = releaseDate,
        revenue = 0,
        runtime = 0,
        spokenLanguages = emptyList(),
        status = "",
        tagline = "",
        title = title,
        video = false,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isInWatchlist = isInWatchlist
    )
}

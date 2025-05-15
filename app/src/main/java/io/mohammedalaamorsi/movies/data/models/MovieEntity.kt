package io.mohammedalaamorsi.movies.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String? = null,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int
)

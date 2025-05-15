package io.mohammedalaamorsi.movies.data.models


import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

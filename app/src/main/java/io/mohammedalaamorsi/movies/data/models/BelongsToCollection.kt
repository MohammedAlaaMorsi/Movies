package io.mohammedalaamorsi.movies.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BelongsToCollection(
    @SerialName("backdrop_path")
    val backdropPath: String?=null,
    val id: Int,
    val name: String,
    @SerialName("poster_path")
    val posterPath: String?=null
)

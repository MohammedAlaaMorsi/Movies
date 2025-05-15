package io.mohammedalaamorsi.movies.data.models


import kotlinx.serialization.Serializable

@Serializable
data class Credit(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)

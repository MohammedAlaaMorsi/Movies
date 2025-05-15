package io.mohammedalaamorsi.movies.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed interface Screens {
    @Parcelize
    @Serializable
    data object MoviesList : Screens, Parcelable

    @Parcelize
    @Serializable
    data class MoviesDetail(
        val movieId: Int
    ) : Screens, Parcelable
}

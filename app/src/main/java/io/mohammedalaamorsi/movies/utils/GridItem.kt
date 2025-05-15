package io.mohammedalaamorsi.movies.utils

sealed class GridItem {
    data class MovieItem(
        val id: Int,
        val title: String,
        val overview:String,
        val imageUrl: String,
        val rating: Double,
        val releaseDate: String,
        val isInWatchlist: Boolean = false
    ) : GridItem()

    data class HeaderItem(
        val title: UiText,
    ) : GridItem()
}

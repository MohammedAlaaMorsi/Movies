package io.mohammedalaamorsi.movies.data.remote


class MoviesUrlProvider:UrlsProvider {
    override fun searchMovies(
        searchQuery: String,
        includeAdult: Boolean,
        language: String,
        page: Int
    ): String {
        return "https://api.themoviedb.org/3/search/movie?query=${searchQuery}&include_adult=$includeAdult&language=$language&page=$page"

    }

    override fun getPopularMovies(language: String, page: Int): String {
       return "https://api.themoviedb.org/3/movie/popular?language=$language&page=$page"

    }

    override fun getMovieDetails(movieId: Int, language: String): String {
       return "https://api.themoviedb.org/3/movie/$movieId?language=$language"
    }

    override fun getSimilarMovies(movieId: Int, language: String, page: Int): String {
       return "https://api.themoviedb.org/3/movie/$movieId/similar?language=$language&page=$page"
    }

    override fun getMovieCredits(movieId: Int, language: String): String {
      return "https://api.themoviedb.org/3/movie/$movieId/credits?language=$language"
    }
}

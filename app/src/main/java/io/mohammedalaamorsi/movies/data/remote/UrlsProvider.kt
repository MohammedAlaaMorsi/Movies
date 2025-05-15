package io.mohammedalaamorsi.movies.data.remote


interface UrlsProvider {

    //("https://api.themoviedb.org/3/search/movie?query=hello&include_adult=false&language=en-US&page=1")
    fun searchMovies(searchQuery:String,includeAdult:Boolean=false,language: String="en-US",page: Int=1): String
    //("https://api.themoviedb.org/3/movie/popular?language=en-US&page=1")
    fun getPopularMovies(language: String="en-US", page: Int=1): String
    //("https://api.themoviedb.org/3/movie/1098152?language=en-US")
    fun getMovieDetails(movieId: Int, language: String="en-US"): String
    //("https://api.themoviedb.org/3/movie/1098152/similar?language=en-US&page=1")
    fun getSimilarMovies(movieId: Int,language: String="en-US", page: Int=1): String
    //("https://api.themoviedb.org/3/movie/1098152/credits?language=en-US")
    fun getMovieCredits(movieId: Int,language: String="en-US"): String
}

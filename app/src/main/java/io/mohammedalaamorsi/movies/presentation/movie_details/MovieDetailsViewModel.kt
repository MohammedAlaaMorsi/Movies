package io.mohammedalaamorsi.movies.presentation.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mohammedalaamorsi.movies.R
import io.mohammedalaamorsi.movies.data.models.Credit
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.data.models.MovieDetails
import io.mohammedalaamorsi.movies.domain.usecase.AddMovieToWatchlistUseCase
import io.mohammedalaamorsi.movies.domain.usecase.GetMovieCreditsUseCase
import io.mohammedalaamorsi.movies.domain.usecase.GetMovieDetailsUseCase
import io.mohammedalaamorsi.movies.domain.usecase.GetSimilarMoviesUseCase
import io.mohammedalaamorsi.movies.domain.usecase.RemoveMovieFromWatchlistUseCase
import io.mohammedalaamorsi.movies.presentation.states.MovieDetailsState
import io.mohammedalaamorsi.movies.presentation.states.events.MovieDetailsEvent
import io.mohammedalaamorsi.movies.utils.UiText
import io.mohammedalaamorsi.movies.utils.toMovieEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MovieDetailsViewModel(
    movieId: Int,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase,
    private val addMovieToWatchlistUseCase: AddMovieToWatchlistUseCase,
    private val removeMovieFromWatchlistUseCase: RemoveMovieFromWatchlistUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MovieDetailsState())
    val state: StateFlow<MovieDetailsState> = _state.asStateFlow()
  
    init {
        onEvent(MovieDetailsEvent.GetMovieDetails(movieId))
    }

    fun onEvent(event: MovieDetailsEvent) = viewModelScope.launch {
        when (event) {
            is MovieDetailsEvent.GetMovieDetails -> {
                loadMovieDetailsSection(event.movieId)
                loadSimilarMoviesSection(event.movieId)
                loadCastSection(event.movieId)
            }
            is MovieDetailsEvent.AddToWatchList -> {
                toggleWatchlistStatus(event.movieDetails)
            }
        }
    }

    private fun loadMovieDetailsSection(movieId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            getMovieDetailsUseCase(movieId)
                .catch { error ->
                    _state.update {
                        it.copy(
                            isLoading = false, 
                            error = R.string.failed_to_load_movie_details
                        )
                    }
                }
                .collectLatest { movieDetails ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            movieDetails = movieDetails,
                            isInWatchlist = movieDetails.isInWatchlist, 
                            error = R.string.empty_string
                        )
                    }
                }
        }
    }

    private fun loadSimilarMoviesSection(movieId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isSimilarMoviesLoading = true) }
            
            getSimilarMoviesUseCase(movieId)
                .catch { error ->
                    _state.update {
                        it.copy(
                            isSimilarMoviesLoading = false, 
                            similarMoviesError = R.string.failed_to_load_similar_movies
                        )
                    }
                }
                .collectLatest { allSimilarMovies ->
                    val topFiveSimilarMovies = allSimilarMovies.take(5)
                    
                    _state.update {
                        it.copy(
                            isSimilarMoviesLoading = false,
                            similarMovies = topFiveSimilarMovies,
                            similarMoviesError =  R.string.empty_string
                        )
                    }
                }
        }
    }

    private fun loadCastSection(movieId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isCastLoading = true) }
            
            val mainMovieCredits = async { fetchMovieCredits(movieId) }

            var similarMovieIds = emptyList<Int>()
            val waitForSimilarMovies = launch {
                while (_state.value.similarMovies.isEmpty() && _state.value.isSimilarMoviesLoading) {
                    delay(100)
                }
                similarMovieIds = _state.value.similarMovies.map { it.id }
            }
            
            waitForSimilarMovies.join()

            val allCredits = mutableListOf<Credit>()
            mainMovieCredits.await()?.let { allCredits.add(it) }

            for (similarMovieId in similarMovieIds) {
                val credit = fetchMovieCredits(similarMovieId)
                credit?.let { allCredits.add(it) }
            }

            processAllCredits(allCredits)
        }
    }
    
    private suspend fun fetchMovieCredits(movieId: Int): Credit? {
        var credit: Credit? = null
        try {
            getMovieCreditsUseCase(movieId)
                .catch { /* Ignore errors for individual movies */ }
                .collectLatest { movieCredit ->
                    credit = movieCredit
                }
        } catch (e: Exception) {
            // Ignore individual movie credit fetch errors
        }
        return credit
    }
    
    private fun processAllCredits(allCredits: List<Credit>) {
        if (allCredits.isEmpty()) {
            _state.update {
                it.copy(
                    isCastLoading = false,
                    castError = R.string.no_cast_information_available
                )
            }
            return
        }
        
        val allCast = allCredits.flatMap { it.cast }
        val allCrew = allCredits.flatMap { it.crew }
        
        val castByDepartment = allCast.groupBy { it.knownForDepartment }
        
        val crewByDepartment = allCrew.groupBy { it.department }
        
        val topActors = castByDepartment["Acting"]?.sortedByDescending { it.popularity }?.take(5) ?: emptyList()
        
        val directors = crewByDepartment["Directing"]?.filter { it.job == "Director" } ?: emptyList()
        val topDirectors = directors.sortedByDescending { it.popularity }.take(5)
        
        _state.update {
            it.copy(
                isCastLoading = false,
                castMembers = castByDepartment,
                crewMembers = crewByDepartment,
                topActors = topActors,
                topDirectors = topDirectors,
                castError =  R.string.empty_string
            )
        }
    }
    
    private fun toggleWatchlistStatus(movieDetails: MovieDetails) {
        viewModelScope.launch {
            val currentMovieDetails = _state.value.movieDetails ?: return@launch
            val isInWatchlist = _state.value.isInWatchlist
            val movieId = movieDetails.id
            
            try {
                val isMainMovie = currentMovieDetails.id == movieId
                
                if (isInWatchlist) {
                    removeMovieFromWatchlistUseCase(movieId = movieId).collect { success ->
                        if (success) {
                            if (isMainMovie) {
                                val updatedMovieDetails = currentMovieDetails.copy(isInWatchlist = false)
                                _state.update { 
                                    it.copy(
                                        isInWatchlist = false,
                                        movieDetails = updatedMovieDetails
                                    )
                                }
                            }
                            updateSimilarMoviesWatchlistStatus(movieId, false)
                            
                            if ( currentMovieDetails.id == movieId) {
                                val updatedMainMovie = currentMovieDetails.copy(isInWatchlist = false)
                                _state.update { 
                                    it.copy(
                                        isInWatchlist = false,
                                        movieDetails = updatedMainMovie
                                    )
                                }
                            }
                        }
                    }
                } else {
                    addMovieToWatchlistUseCase(movieDetails.toMovieEntity()).collect { success ->
                        if (success) {
                            if (isMainMovie) {
                                val updatedMovieDetails = currentMovieDetails.copy(isInWatchlist = true)
                                _state.update { 
                                    it.copy(
                                        isInWatchlist = true,
                                        movieDetails = updatedMovieDetails
                                    )
                                }
                            }
                            updateSimilarMoviesWatchlistStatus(movieId, true)
                            
                            if ( currentMovieDetails.id == movieId) {
                                val updatedMainMovie = currentMovieDetails.copy(isInWatchlist = true)
                                _state.update { 
                                    it.copy(
                                        isInWatchlist = true,
                                        movieDetails = updatedMainMovie
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle any errors that may occur during the watchlist operation

            }
        }
    }

    private fun updateSimilarMoviesWatchlistStatus(movieId: Int, isInWatchlist: Boolean) {
        val currentSimilarMovies = _state.value.similarMovies
        
        val updatedSimilarMovies = currentSimilarMovies.map { movie ->
            if (movie.id == movieId) {
                movie.copy(isInWatchlist = isInWatchlist)
            } else {
                movie
            }
        }
        
        if (updatedSimilarMovies != currentSimilarMovies) {
            _state.update {
                it.copy(similarMovies = updatedSimilarMovies)
            }
        }
    }


}

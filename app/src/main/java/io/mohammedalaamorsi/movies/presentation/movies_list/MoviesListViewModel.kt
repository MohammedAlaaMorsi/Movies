package io.mohammedalaamorsi.movies.presentation.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mohammedalaamorsi.movies.R
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.domain.usecase.GetPopularMoviesUseCase
import io.mohammedalaamorsi.movies.domain.usecase.GetWatchlistMoviesUseCase
import io.mohammedalaamorsi.movies.domain.usecase.SearchMoviesUseCase
import io.mohammedalaamorsi.movies.presentation.states.MoviesState
import io.mohammedalaamorsi.movies.presentation.states.UiState
import io.mohammedalaamorsi.movies.presentation.states.effects.Effect
import io.mohammedalaamorsi.movies.presentation.states.events.MoviesEvent
import io.mohammedalaamorsi.movies.utils.GridItem
import io.mohammedalaamorsi.movies.utils.UiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MoviesListViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getWatchlistMoviesUseCase: GetWatchlistMoviesUseCase
) : ViewModel() {



    private val _effects = MutableSharedFlow<Effect>(replay = 1)
    val effects = _effects.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state



    @OptIn(FlowPreview::class)
    private val searchQuery = _searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = "",
        )

    init {
        onEvent(MoviesEvent.GetPopularMovies)
        observeSearchQuery()
        observeWatchlistMovies()
    }

    private fun observeWatchlistMovies(){
        // Keep track of previous watchlist size to detect changes
        var previousWatchlistSize = -1
        
        viewModelScope.launch {
            getWatchlistMoviesUseCase.invoke()
                .collect { watchlistMovies ->
                    // Get the current state
                    val currentState = _state.value
                    
                    // Check if this is a watchlist change that should be notified
                    val currentWatchlistSize = watchlistMovies.size
                    val isFirstLoad = previousWatchlistSize == -1
                    val watchlistSizeChanged = !isFirstLoad && previousWatchlistSize != currentWatchlistSize
                    
                    // Store current size for next comparison
                    val oldSize = previousWatchlistSize
                    previousWatchlistSize = currentWatchlistSize
                    
                    if (currentState is UiState.Result) {
                        // Create a map of watchlist movie IDs for quick lookup
                        val watchlistMovieIds = watchlistMovies.map { it.id }.toSet()
                        
                        // Update watchlist status in popular movies if they're currently displayed
                        val updatedPopularMovies = currentState.moviesState.popularMovies.map { movie ->
                            movie.copy(isInWatchlist = watchlistMovieIds.contains(movie.id))
                        }
                        
                        // Update watchlist status in search movies if they're currently displayed
                        val updatedSearchMovies = currentState.moviesState.searchMovies.map { movie ->
                            movie.copy(isInWatchlist = watchlistMovieIds.contains(movie.id))
                        }
                        
                        // Update the state with movies that have updated watchlist status
                        val isSearchActive = currentState.moviesState.searchQuery.isNotEmpty()
                        val moviesToDisplay = if (isSearchActive) updatedSearchMovies else updatedPopularMovies
                        
                        _state.value = UiState.Result(
                            MoviesState(
                                popularMovies = updatedPopularMovies,
                                searchMovies = updatedSearchMovies,
                                searchQuery = currentState.moviesState.searchQuery,
                                currentMovies = prepareMovies(moviesToDisplay)
                            )
                        )
                        
                        if (watchlistSizeChanged) {
                            val messageRes = if (currentWatchlistSize > oldSize) 
                                R.string.movie_added_to_watchlist
                            else 
                                R.string.movie_removed_from_watchlist
                            
                            _effects.emit(Effect.ShowSnackbarResource(messageRes))
                        }
                    }
                }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery.collect { query ->
                if (query.length > 2) {
                    _state.value = UiState.Loading
                    searchMoviesUseCase(query).collect { movies ->
                        _state.value = if (movies.isEmpty()) {
                            UiState.Error(R.string.no_result)
                        } else {
                            UiState.Result(
                                MoviesState(
                                    popularMovies = _state.value.moviesState.popularMovies,
                                    searchMovies = movies,
                                    searchQuery = query,
                                    currentMovies = prepareMovies(movies)
                                )
                            )
                        }
                    }
                } else if (query.isEmpty()) {
                    if (_state.value.moviesState.popularMovies.isEmpty()) {
                        getPopularMovies()
                    } else {
                        _state.value = UiState.Result(
                            MoviesState(
                                popularMovies = _state.value.moviesState.popularMovies,
                                searchMovies = emptyList(),
                                searchQuery = "",
                                currentMovies = prepareMovies(_state.value.moviesState.popularMovies)
                            )
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: MoviesEvent) = viewModelScope.launch {
        when (event) {
            MoviesEvent.GetPopularMovies -> {
                getPopularMovies()
            }

            is MoviesEvent.OnSearch -> {
                _searchQuery.value = event.searchQuery
            }

            is MoviesEvent.ShowError -> {
                _effects.emit(
                    Effect.ShowSnackbarResource(messageRes = event.error)
                )
            }
        }
    }

    private suspend fun getPopularMovies() {
        getPopularMoviesUseCase().collect { movies ->
            _state.value = if (movies.isEmpty()) {
                UiState.Error(R.string.no_movies_found)
            } else {
                UiState.Result(
                    MoviesState(
                        popularMovies = movies,
                        searchMovies = emptyList(),
                        searchQuery = "",
                        currentMovies = prepareMovies(movies)
                    )
                )
            }
        }
    }

    private fun prepareMovies(moviesList: List<Movie>): List<GridItem> {
        return moviesList.groupBy { movie ->
            try {
                movie.releaseDate.split("-")[0].toInt()
            } catch (e: Exception) {
                0
            }
        }
            .toSortedMap(reverseOrder())
            .flatMap { (year, moviesInYear) ->
                buildList {
                    add(
                        GridItem.HeaderItem(
                            title = UiText.DynamicString(year.takeIf { it != 0 }?.toString()?:"Unknown year")
                        )
                    )
                    addAll(moviesInYear.map { movie ->
                        GridItem.MovieItem(
                            id = movie.id,
                            title = movie.title,
                            imageUrl = movie.posterPath?: "",
                            releaseDate = movie.releaseDate,
                            rating = movie.voteAverage,
                            overview = movie.overview,
                            isInWatchlist = movie.isInWatchlist
                        )
                    })
                }
            }
    }


} 

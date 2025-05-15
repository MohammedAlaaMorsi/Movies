package io.mohammedalaamorsi.movies.presentation.movies_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.mohammedalaamorsi.movies.presentation.states.UiState
import io.mohammedalaamorsi.movies.presentation.states.effects.Effect
import io.mohammedalaamorsi.movies.presentation.states.events.MoviesEvent
import io.mohammedalaamorsi.movies.presentation.ui.LoadingIndicator
import io.mohammedalaamorsi.movies.presentation.ui.MoviesGridWithHeader
import io.mohammedalaamorsi.movies.presentation.ui.SearchComponent
import org.koin.androidx.compose.koinViewModel


@Composable
fun MoviesListScreen(
    viewModel: MoviesListViewModel = koinViewModel(),
    onMovieClicked: (Int) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            SearchComponent(onSearch = {
                viewModel.onEvent(MoviesEvent.OnSearch(it))
            }, text = state.moviesState.searchQuery)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (state) {
                is UiState.Error -> {
                    viewModel.onEvent(MoviesEvent.ShowError(state.errorMessage))
                }

                is UiState.Loading -> {
                    LoadingIndicator()
                }

                is UiState.Result -> {
                    MoviesGridWithHeader(
                        gridItems = state.moviesState.currentMovies, onMovieClicked
                    )
                }
            }

        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect {
            when (it) {
                is Effect.ShowSnackbarResource -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(it.messageRes),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}

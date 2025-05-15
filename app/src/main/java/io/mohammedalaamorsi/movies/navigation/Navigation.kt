package io.mohammedalaamorsi.movies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.mohammedalaamorsi.movies.presentation.movie_details.MovieDetailsScreen
import io.mohammedalaamorsi.movies.presentation.movie_details.MovieDetailsViewModel
import io.mohammedalaamorsi.movies.presentation.movies_list.MoviesListScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun RootNavHost(modifier: Modifier = Modifier) {

    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Screens.MoviesList,
        modifier = modifier,
    ) {
        composable<Screens.MoviesList> {
            MoviesListScreen(
                onMovieClicked = { movieId ->
                    navHostController.navigate(
                        Screens.MoviesDetail(movieId)
                    )
                }
            )
        }
        composable<Screens.MoviesDetail> { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId", 0)

            val movieDetailsViewModel = koinViewModel<MovieDetailsViewModel> {
                parametersOf(movieId)
            }
            MovieDetailsScreen(
                viewModel = movieDetailsViewModel,
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}

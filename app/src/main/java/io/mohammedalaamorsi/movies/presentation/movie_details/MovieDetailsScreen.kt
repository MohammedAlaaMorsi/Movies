package io.mohammedalaamorsi.movies.presentation.movie_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.mohammedalaamorsi.movies.R
import io.mohammedalaamorsi.movies.data.models.Cast
import io.mohammedalaamorsi.movies.data.models.Crew
import io.mohammedalaamorsi.movies.data.models.Movie
import io.mohammedalaamorsi.movies.data.models.MovieDetails
import io.mohammedalaamorsi.movies.presentation.states.MovieDetailsState
import androidx.compose.material.icons.Icons

import io.mohammedalaamorsi.movies.presentation.states.events.MovieDetailsEvent
import io.mohammedalaamorsi.movies.presentation.ui.ErrorMessage
import io.mohammedalaamorsi.movies.presentation.ui.InfoItem
import io.mohammedalaamorsi.movies.presentation.ui.LoadingIndicator
import io.mohammedalaamorsi.movies.utils.UiText
import io.mohammedalaamorsi.movies.utils.formatCurrency
import io.mohammedalaamorsi.movies.utils.toImageUrl
import io.mohammedalaamorsi.movies.utils.toMovieDetails

@Composable
fun MovieDetailsScreen(viewModel: MovieDetailsViewModel, onNavigateBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = state.movieDetails?.title ?: "Movie Details",
                onNavigateBack = onNavigateBack,
                isInWatchlist = state.isInWatchlist,
                onToggleWatchlist = {
                    // Only trigger the event if we have movie details
                    state.movieDetails?.let { MovieDetailsEvent.AddToWatchList(it) }
                        ?.let { viewModel.onEvent(it) }
                }
            )
        }
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingIndicator()
        } else if (state.error != R.string.empty_string) {
            ErrorMessage(message = stringResource(id = state.error))
        } else {
            MovieDetailsContent(
                viewModel::onEvent,
                state = state,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun TopAppBar(
    title: String,
    onNavigateBack: () -> Unit,
    isInWatchlist: Boolean,
    onToggleWatchlist: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        
        IconButton(onClick = onToggleWatchlist) {
            Icon(
                imageVector = if (isInWatchlist) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isInWatchlist) "Remove from watchlist" else "Add to watchlist",
                tint = if (isInWatchlist) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
fun MovieDetailsContent(
    onEvent: (MovieDetailsEvent) -> Unit,
    state: MovieDetailsState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Section 1: Movie Details
        state.movieDetails?.let { movieDetails ->
            MovieDetailsSection(movieDetails = movieDetails)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        
        SimilarMoviesSection(
            onEvent = onEvent,
            movies = state.similarMovies,
            isLoading = state.isSimilarMoviesLoading,
            error = if (state.similarMoviesError != R.string.empty_string) stringResource(id = state.similarMoviesError) else null
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider()
        
        CastSection(
            topActors = state.topActors,
            topDirectors = state.topDirectors,
            isLoading = state.isCastLoading,
            error = if (state.castError != R.string.empty_string) stringResource(id = state.castError) else null
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun MovieDetailsSection(movieDetails: MovieDetails) {
    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            AsyncImage(
                model = movieDetails.backdropPath?.toImageUrl() ?: movieDetails.posterPath?.toImageUrl(),
                contentDescription = movieDetails.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = movieDetails.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                
                if (movieDetails.tagline.isNotEmpty()) {
                    Text(
                        text = movieDetails.tagline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            InfoItem(label = "Release Date", value = movieDetails.releaseDate)
            InfoItem(label = "Status", value = movieDetails.status)
            InfoItem(
                label = "Rating", 
                value = String.format("%.1f/10", movieDetails.voteAverage)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (movieDetails.revenue > 0) {
            Text(
                text = "Revenue: ${movieDetails.revenue.formatCurrency()}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        Text(
            text = stringResource(R.string.overview),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = movieDetails.overview,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun SimilarMoviesSection(
    onEvent: (MovieDetailsEvent) -> Unit,
    movies: List<Movie>,
    isLoading: Boolean,
    error: String?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Similar Movies",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }
            }
            error != null -> {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            movies.isEmpty() -> {
                Text(
                    text = "No similar movies found",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            else -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(movies) { movie ->
                        SimilarMovieItem(
                            movie = movie,
                            onToggleWatchlist = { 
                                onEvent(MovieDetailsEvent.AddToWatchList(movie.toMovieDetails()))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimilarMovieItem(
    movie: Movie,
    onToggleWatchlist: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(250.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            // Movie poster with watchlist indicator
            Box {
                AsyncImage(
                    model = movie.posterPath?.toImageUrl(),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                )
                
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .size(28.dp)
                        .clickable { onToggleWatchlist() },
                    shape = CircleShape,
                    color = if (movie.isInWatchlist) 
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f)
                    else 
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                ) {
                    Icon(
                        imageVector = if (movie.isInWatchlist) 
                            Icons.Default.Favorite 
                        else 
                            Icons.Filled.FavoriteBorder,
                        contentDescription = if (movie.isInWatchlist) 
                            stringResource(R.string.remove_from_watchlist) 
                        else 
                            stringResource(R.string.add_to_watchlist),
                        tint = if (movie.isInWatchlist) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .padding(6.dp)
                            .size(16.dp)
                    )
                }
            }
            
            Text(
                text = movie.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun CastSection(
    topActors: List<Cast>,
    topDirectors: List<Crew>,
    isLoading: Boolean,
    error: String?
) {
    Column(modifier = Modifier.padding(16.dp)) {
        when {
            isLoading -> {
                Text(
                    text = stringResource(R.string.top_cast_crew),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp))
                }
            }
            error != null -> {
                Text(
                    text = stringResource(R.string.top_cast_crew),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            topActors.isEmpty() && topDirectors.isEmpty() -> {
                Text(
                    text = stringResource(R.string.no_cast_information_available),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            else -> {
                Text(
                    text = stringResource(R.string.top_cast_crew),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (topActors.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.top_actors),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(topActors) { actor ->
                            PersonItem(
                                name = actor.name,
                                profilePath = actor.profilePath,
                                subtitle = actor.character
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                if (topDirectors.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.top_directors),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(topDirectors) { director ->
                            PersonItem(
                                name = director.name,
                                profilePath = director.profilePath,
                                subtitle = director.job
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonItem(name: String, profilePath: String?, subtitle: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            AsyncImage(
                model = profilePath?.toImageUrl(),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

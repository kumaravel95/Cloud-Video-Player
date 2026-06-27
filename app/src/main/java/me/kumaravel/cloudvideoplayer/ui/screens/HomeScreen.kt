package me.kumaravel.cloudvideoplayer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import coil.compose.AsyncImage
import me.kumaravel.cloudvideoplayer.data.model.Movie
import me.kumaravel.cloudvideoplayer.ui.UIState

@Composable
fun HomeScreen(
    uiState: UIState,
    onRefresh: () -> Unit,
    onSettingsClick: () -> Unit,
    onMovieClick: (Movie) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cloud Video Player",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = onRefresh) {
                        Text("Refresh")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    // This now works because onSettingsClick is a parameter
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (uiState) {
                is UIState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Loading...", color = Color.White)
                    }
                }
                is UIState.Success -> {
                    MovieGrid(movies = uiState.movies, onMovieClick = onMovieClick)
                }
                is UIState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No movies found", color = Color.White)
                    }
                }
                is UIState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${uiState.message}", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieGrid(movies: List<Movie>, onMovieClick: (Movie) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movie = movie, onClick = { onMovieClick(movie) })
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(16f / 9f)
            .fillMaxWidth(),
        // Removed 'color' parameter from Glow
        glow = CardDefaults.glow(
            focusedGlow = Glow(
                elevationColor = Color.Transparent, // or any color like Color.White.copy(alpha = 0.5f)
                elevation = 8.dp
            )
        ),
        scale = CardDefaults.scale(focusedScale = 1.1f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (movie.poster != null) {
                AsyncImage(
                    model = movie.poster,
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // Title overlay at bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

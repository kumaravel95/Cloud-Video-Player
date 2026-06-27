package me.kumaravel.cloudvideoplayer.ui.screens

import android.content.pm.PackageManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    val context = LocalContext.current
    val isTv = remember { context.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK) }

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
                if (isTv) {
                    androidx.tv.material3.Text(
                        text = "Cloud Video Player",
                        style = androidx.tv.material3.MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                } else {
                    androidx.compose.material3.Text(
                        text = "Cloud Video Player",
                        style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isTv) {
                        androidx.tv.material3.Button(onClick = onRefresh) {
                            androidx.tv.material3.Text("Refresh")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        androidx.tv.material3.IconButton(onClick = onSettingsClick) {
                            androidx.tv.material3.Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    } else {
                        androidx.compose.material3.Button(onClick = onRefresh) {
                            androidx.compose.material3.Text("Refresh")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        androidx.compose.material3.IconButton(onClick = onSettingsClick) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (uiState) {
                is UIState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (isTv) {
                            androidx.tv.material3.Text("Loading...", color = Color.White)
                        } else {
                            androidx.compose.material3.Text("Loading...", color = Color.White)
                        }
                    }
                }
                is UIState.Success -> {
                    MovieGrid(movies = uiState.movies, isTv = isTv, onMovieClick = onMovieClick)
                }
                is UIState.Empty -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (isTv) {
                            androidx.tv.material3.Text("No movies found", color = Color.White)
                        } else {
                            androidx.compose.material3.Text("No movies found", color = Color.White)
                        }
                    }
                }
                is UIState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (isTv) {
                            androidx.tv.material3.Text("Error: ${uiState.message}", color = Color.Red)
                        } else {
                            androidx.compose.material3.Text("Error: ${uiState.message}", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieGrid(movies: List<Movie>, isTv: Boolean, onMovieClick: (Movie) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movie = movie, isTv = isTv, onClick = { onMovieClick(movie) })
        }
    }
}

@OptIn(androidx.tv.material3.ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCard(movie: Movie, isTv: Boolean, onClick: () -> Unit) {
    if (isTv) {
        androidx.tv.material3.Card(
            onClick = onClick,
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .fillMaxWidth(),
            glow = androidx.tv.material3.CardDefaults.glow(
                focusedGlow = androidx.tv.material3.Glow(
                    elevationColor = Color.Transparent,
                    elevation = 8.dp
                )
            ),
            scale = androidx.tv.material3.CardDefaults.scale(focusedScale = 1.1f)
        ) {
            MovieCardContent(movie = movie, isTv = isTv)
        }
    } else {
        androidx.compose.material3.Card(
            onClick = onClick,
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .fillMaxWidth()
        ) {
            MovieCardContent(movie = movie, isTv = isTv)
        }
    }
}

@Composable
fun MovieCardContent(movie: Movie, isTv: Boolean) {
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
                if (isTv) {
                    androidx.tv.material3.Text(
                        text = movie.title,
                        style = androidx.tv.material3.MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    androidx.compose.material3.Text(
                        text = movie.title,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
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
            if (isTv) {
                androidx.tv.material3.Text(
                    text = movie.title,
                    style = androidx.tv.material3.MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            } else {
                androidx.compose.material3.Text(
                    text = movie.title,
                    style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

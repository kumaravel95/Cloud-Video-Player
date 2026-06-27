package me.kumaravel.cloudvideoplayer

import SettingsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import me.kumaravel.cloudvideoplayer.data.remote.MovieApi
import me.kumaravel.cloudvideoplayer.data.repository.MovieRepository
import me.kumaravel.cloudvideoplayer.ui.MovieViewModel
import me.kumaravel.cloudvideoplayer.ui.MovieViewModelFactory
import me.kumaravel.cloudvideoplayer.ui.screens.HomeScreen
import me.kumaravel.cloudvideoplayer.ui.screens.PlayerScreen
import me.kumaravel.cloudvideoplayer.ui.theme.CloudVideoPlayerTheme
import me.kumaravel.cloudvideoplayer.util.Constants
import me.kumaravel.cloudvideoplayer.util.SettingsManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        val settingsManager = SettingsManager(applicationContext)
        val api = retrofit.create(MovieApi::class.java)
        val repository = MovieRepository(api, settingsManager)
        val viewModelFactory = MovieViewModelFactory(repository, settingsManager)

        setContent {
            CloudVideoPlayerTheme {
                val navController = rememberNavController()
                val viewModel: MovieViewModel = viewModel(factory = viewModelFactory)
                val uiState by viewModel.uiState.collectAsState()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            uiState = uiState,
                            onRefresh = { viewModel.fetchMovies() },
                            onMovieClick = { movie ->
                                val encodedUrl = URLEncoder.encode(movie.url, StandardCharsets.UTF_8.toString())
                                navController.navigate("player/$encodedUrl")
                            },
                            onSettingsClick = {
                                navController.navigate("settings")
                            }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            settingsManager = settingsManager,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = "player/{videoUrl}",
                        arguments = listOf(navArgument("videoUrl") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val videoUrl = backStackEntry.arguments?.getString("videoUrl") ?: ""
                        PlayerScreen(
                            videoUrl = videoUrl,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

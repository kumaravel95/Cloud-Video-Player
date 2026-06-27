package me.kumaravel.cloudvideoplayer.data.repository

import kotlinx.coroutines.flow.first
import me.kumaravel.cloudvideoplayer.data.model.Movie
import me.kumaravel.cloudvideoplayer.data.remote.MovieApi
import me.kumaravel.cloudvideoplayer.util.SettingsManager

class MovieRepository(
    private val api: MovieApi,
    private val settingsManager: SettingsManager
) {
    suspend fun getMovies(): List<Movie> {
        // Retrieve the current URL from DataStore (first() gets the current value and stops collecting)
        val currentUrl = settingsManager.configUrl.first()

        // Pass the dynamic URL to the API call
        return api.getMovies(currentUrl).movies
    }
}
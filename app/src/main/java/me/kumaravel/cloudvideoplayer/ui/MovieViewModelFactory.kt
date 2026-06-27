package me.kumaravel.cloudvideoplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.kumaravel.cloudvideoplayer.data.repository.MovieRepository
import me.kumaravel.cloudvideoplayer.util.SettingsManager

class MovieViewModelFactory(
    private val repository: MovieRepository,
    private val settingsManager: SettingsManager // Added this parameter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Pass both arguments to the MovieViewModel constructor
            return MovieViewModel(repository, settingsManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
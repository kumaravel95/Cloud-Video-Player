package me.kumaravel.cloudvideoplayer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.kumaravel.cloudvideoplayer.data.model.Movie
import me.kumaravel.cloudvideoplayer.data.repository.MovieRepository
import me.kumaravel.cloudvideoplayer.util.SettingsManager

sealed class UIState {
    object Loading : UIState()
    data class Success(val movies: List<Movie>) : UIState()
    data class Error(val message: String) : UIState()
    object Empty : UIState()
}

class MovieViewModel(
    private val repository: MovieRepository,
    private val settingsManager: SettingsManager // Added SettingsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        // Observe the configuration URL from DataStore
        viewModelScope.launch {
            settingsManager.configUrl.collect { url ->
                // Whenever the URL changes (or on app start), fetch movies
                fetchMovies()
            }
        }
    }

    fun fetchMovies() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            try {
                // The repository is now responsible for getting the URL from SettingsManager
                val movies = repository.getMovies()
                if (movies.isEmpty()) {
                    _uiState.value = UIState.Empty
                } else {
                    _uiState.value = UIState.Success(movies)
                }
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}
package me.kumaravel.cloudvideoplayer.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val title: String,
    val url: String,
    val poster: String? = null,
    val description: String? = null
)

@Serializable
data class MovieListResponse(
    val movies: List<Movie>
)

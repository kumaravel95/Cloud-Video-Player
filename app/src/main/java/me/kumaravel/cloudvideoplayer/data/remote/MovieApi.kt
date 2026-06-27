package me.kumaravel.cloudvideoplayer.data.remote

import me.kumaravel.cloudvideoplayer.data.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface MovieApi {
    @GET
    suspend fun getMovies(@Url url: String): MovieListResponse
}

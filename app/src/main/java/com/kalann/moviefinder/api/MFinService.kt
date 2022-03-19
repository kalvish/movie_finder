package com.kalann.moviefinder.api

import com.kalann.moviefinder.api.moshi.Movies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MFinService {
    @GET("3/movie/{type}")
    suspend fun getMovies(@Path("type") type : String? = null,
                              @Query("page") page : String,
    @Query("api_key") apiKey : String): Movies
}
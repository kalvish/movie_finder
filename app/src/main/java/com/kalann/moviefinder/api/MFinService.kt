package com.kalann.moviefinder.api

import com.kalann.moviefinder.api.moshi.Movies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MFinService {
    object Instance{
        val API_KEY = "0e7274f05c36db12cbe71d9ab0393d47"
        val MOVIEDB_STARTING_PAGE_INDEX = 1
    }

    enum class MovieApiNetworkStatus {
        LOADING,
        SUCCESS,
        ERROR
    }

    enum class PageTypes (val value: String?) {
        NOW_PLAYING("now_playing"),
        POPULAR("popular"),
        TOP_RATED("top_rated"),
        UPCOMING("upcoming")
    }

    @GET("3/movie/{type}")
    suspend fun getMovies(@Path("type") type : String? = null,
                          @Query("page") page : Int,
                          @Query("api_key") apiKey : String): Movies

    @GET("3/search/movie")
    suspend fun searchMovies(@Query("query") query: String,
                             @Query("page") page : Int,
                             @Query("api_key") apiKey : String): Movies
}
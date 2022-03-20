package com.kalann.moviefinder.movies

import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.dagger.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class MovieManager @Inject constructor(private val mFinService: MFinService){
    suspend fun getForType(type: MFinService.PageTypes,
    page: Int) = mFinService.getMovies(type.value,page,MFinService.Instance.API_KEY)

    suspend fun searchMovies(query: String,
                           page: Int) = mFinService.searchMovies(query,page,MFinService.Instance.API_KEY)
}
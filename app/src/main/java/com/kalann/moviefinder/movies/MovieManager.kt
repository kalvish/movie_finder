package com.kalann.moviefinder.movies

import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.dagger.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class MovieManager @Inject constructor(private val mFinService: MFinService){
    suspend fun getForType(type: MFinService.PageTypes,
    page: String) = mFinService.getMovies(type.value,page,MFinService.MFin.API_KEY)
}
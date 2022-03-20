package com.kalann.moviefinder.movies

import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.dagger.MoviesScope
import javax.inject.Inject

@MoviesScope
class MoviesViewModel @Inject constructor(val movieManager: MovieManager) {
    suspend fun getForType(type: MFinService.PageTypes,
                           page: Int) = movieManager.getForType(type, page)
}
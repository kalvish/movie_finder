package com.kalann.moviefinder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.movies.MovieDataRepository
import java.lang.IllegalArgumentException

class MoviesViewModelLifecycleFactory (val movieDataRepository: MovieDataRepository
                                       , val feedType: MFinService.PageTypes)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MoviesViewModelLifecycle::class.java)) {
            return MoviesViewModelLifecycle(movieDataRepository, feedType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
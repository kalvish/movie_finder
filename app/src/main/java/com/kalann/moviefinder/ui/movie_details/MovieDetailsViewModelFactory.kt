package com.kalann.moviefinder.ui.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kalann.moviefinder.movies.MovieDataRepository
import java.lang.IllegalArgumentException

class MovieDetailsViewModelFactory (val movieDataRepository: MovieDataRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            return MovieDetailsViewModel(movieDataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
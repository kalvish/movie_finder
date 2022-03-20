package com.kalann.moviefinder.ui.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.movies.MovieDataRepository
import com.kalann.moviefinder.movies.MoviesViewModel
import com.kalann.moviefinder.ui.now_playing.NowPlayingViewModel
import java.lang.IllegalArgumentException

class PopularViewModelFactory (val movieDataRepository: MovieDataRepository
                               , val feedType: MFinService.PageTypes)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PopularViewModel::class.java)) {
            return PopularViewModel(movieDataRepository, feedType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
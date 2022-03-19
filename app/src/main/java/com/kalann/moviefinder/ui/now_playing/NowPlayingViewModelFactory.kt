package com.kalann.moviefinder.ui.now_playing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kalann.moviefinder.movies.MoviesViewModel
import java.lang.IllegalArgumentException

class NowPlayingViewModelFactory (val moviesViewModel: MoviesViewModel)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NowPlayingViewModel::class.java)) {
            return NowPlayingViewModel(moviesViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
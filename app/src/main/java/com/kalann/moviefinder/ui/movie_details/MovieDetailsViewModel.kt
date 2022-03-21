package com.kalann.moviefinder.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.movies.MovieDataRepository
import kotlinx.coroutines.launch

class MovieDetailsViewModel(val movieDataRepository: MovieDataRepository) : ViewModel() {
    private val _networkStatus = MutableLiveData<MFinService.MovieApiNetworkStatus>()
    val networkStatus : LiveData<MFinService.MovieApiNetworkStatus>
        get() = _networkStatus

    private val _movie = MutableLiveData<Movie>()
    val movie : LiveData<Movie>
        get() = _movie

    fun getMovie(id: Int) : LiveData<Movie> {
        viewModelScope.launch {
            getMovieFromManager(id)
        }
        return movie
    }

    private suspend fun getMovieFromManager(id: Int){
        _networkStatus.value = MFinService.MovieApiNetworkStatus.LOADING
        try {
            val result = movieDataRepository.getMovie(id)
            _movie.value = result
            _networkStatus.value = MFinService.MovieApiNetworkStatus.SUCCESS
        } catch (e: Exception) {
            _networkStatus.value = MFinService.MovieApiNetworkStatus.ERROR
        }
    }
}
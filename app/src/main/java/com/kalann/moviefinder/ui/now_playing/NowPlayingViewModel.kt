package com.kalann.moviefinder.ui.now_playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.api.moshi.Movies
import com.kalann.moviefinder.movies.MoviesViewModel
import kotlinx.coroutines.launch

class NowPlayingViewModel(val moviesViewModel: MoviesViewModel): ViewModel() {
    private val _networkStatus = MutableLiveData<MFinService.MovieApiNetworkStatus>()
    val networkStatus : LiveData<MFinService.MovieApiNetworkStatus>
        get() = _networkStatus

    private val _moviesList = MutableLiveData<List<Movie>>()
    val moviesList : LiveData<List<Movie>>
        get() = _moviesList

    fun getMoviesForType(type: MFinService.PageTypes, page: String) : LiveData<List<Movie>> {
        viewModelScope.launch {
            getMoviesFromManager(type, page)
        }
        return moviesList
    }

    private suspend fun getMoviesFromManager(type: MFinService.PageTypes, page: String){
        _networkStatus.value = MFinService.MovieApiNetworkStatus.LOADING
        try {
            val results = moviesViewModel.getForType(type, page)
            _moviesList.value = results.results!!
            _networkStatus.value = MFinService.MovieApiNetworkStatus.SUCCESS
        } catch (e: Exception) {
            _networkStatus.value = MFinService.MovieApiNetworkStatus.ERROR
        }
    }
}
package com.kalann.moviefinder.ui.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.movies.MovieDataRepository
import kotlinx.coroutines.flow.Flow

class PopularViewModel(val movieDataRepository: MovieDataRepository
                       , feedType: MFinService.PageTypes) : ViewModel() {
    val pagingDataFlow: Flow<PagingData<Movie>> = movieDataRepository.getFeedResultStream(feedType)
    .cachedIn(viewModelScope)
}
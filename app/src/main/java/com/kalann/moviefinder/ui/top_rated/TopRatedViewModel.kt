package com.kalann.moviefinder.ui.top_rated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.movies.MovieDataRepository
import kotlinx.coroutines.flow.Flow

class TopRatedViewModel (val movieDataRepository: MovieDataRepository
                         , feedType: MFinService.PageTypes) : ViewModel() {
    val pagingDataFlow: Flow<PagingData<Movie>> = movieDataRepository.getFeedResultStream(feedType)
        .cachedIn(viewModelScope)
}
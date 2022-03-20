package com.kalann.moviefinder.movies

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.dagger.MoviesScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@MoviesScope
class MovieDataRepository @Inject constructor(val movieManager: MovieManager) {
    suspend fun getForType(type: MFinService.PageTypes,
                           page: Int) = movieManager.getForType(type,page)

    fun getSearchResultStream(query: String): Flow<PagingData<Movie>> {
        Log.d("MovieDataRepository", "New query: $query")
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(movieManager, query) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}
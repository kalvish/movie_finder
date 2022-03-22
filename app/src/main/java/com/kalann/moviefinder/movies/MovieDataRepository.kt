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
    suspend fun getMovie(id: Int) = movieManager.getMovieFromApi(id)

    suspend fun getGenreListFromApi() = movieManager.getGenreListFromApi()

    suspend fun getForType(type: MFinService.PageTypes,
                           page: Int) = movieManager.getForType(type,page)

    fun getSearchResultStream(query: String): Flow<PagingData<Movie>> {
        Log.d("MovieDataRepository", "getSearchResultStream New query: $query")
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { MovieSearchPagingSource(movieManager, query) }
        ).flow
    }

    fun getFeedResultStream(feedType: MFinService.PageTypes): Flow<PagingData<Movie>> {
        Log.d("MovieDataRepository", "getFeedResultStream New Type: $feedType")
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { MovieFeedPagingSource(movieManager, feedType) }
        ).flow
    }

    fun getDbFeedResultStream(): Flow<PagingData<Movie>> {
        Log.d("MovieDataRepository", "getDbFeedResultStream")
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { MovieDbFeedPagingSource(movieManager) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    fun saveMovieToDb(movie: Movie) = movieManager.saveMovieToDb(movie)

    fun getMovieForIdFromDb(id: Int) = movieManager.getMovieForIdFromDb(id)

    fun deleteMovieFromDb(id: Int) = movieManager.deleteMovieFromDb(id)

    fun getMoviesAllFlow() = movieManager.getMoviesAllFlow()

    suspend fun apiToDbGenres() = movieManager.apiToDbGenres()
}
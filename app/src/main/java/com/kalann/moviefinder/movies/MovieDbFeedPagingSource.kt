package com.kalann.moviefinder.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.db.MovieDatabase
import retrofit2.HttpException
import java.io.IOException

class MovieDbFeedPagingSource (
    private val movieManager: MovieManager) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: MovieDatabase.Instance.MOVIEDB_STARTING_PAGE_INDEX

        return try {
            val response = movieManager.getMoviesAll(MovieDataRepository.NETWORK_PAGE_SIZE, position)
            val movies = response
            val nextKey = if(movies!!.isEmpty()){
                null
            } else {
                position + MovieDataRepository.NETWORK_PAGE_SIZE
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (position == MovieDatabase.Instance.MOVIEDB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
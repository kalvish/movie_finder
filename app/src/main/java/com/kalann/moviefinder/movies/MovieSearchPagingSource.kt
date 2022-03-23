package com.kalann.moviefinder.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import retrofit2.HttpException
import java.io.IOException

class MovieSearchPagingSource (
    private val movieManager: MovieManager,
    private val query : String) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: MFinService.Instance.MOVIEAPI_STARTING_PAGE_INDEX
        val apiQuery = query
        return try {
            val response = movieManager.searchMovies(apiQuery, position)
            val movies = response.results
            val moviesDuplicate = mutableListOf<Movie>()
            movies?.forEach {
                var movieTemp = it
                it.genre_ids?.let { it1 ->
                    movieTemp.genres = movieManager.getGenresForIds(it1) }
                moviesDuplicate.add(movieTemp)
            }
            val nextKey = if(movies!!.isEmpty()){
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = moviesDuplicate,
                prevKey = if (position == MFinService.Instance.MOVIEAPI_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
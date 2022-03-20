package com.kalann.moviefinder.movies

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import retrofit2.HttpException
import java.io.IOException

class MovieFeedPagingSource (
    private val movieManager: MovieManager,
    private val feedType : MFinService.PageTypes) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: MFinService.Instance.MOVIEDB_STARTING_PAGE_INDEX

        return try {
            val response = movieManager.getForType(feedType, position)
            val movies = response.results
            val nextKey = if(movies!!.isEmpty()){
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (position == MFinService.Instance.MOVIEDB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
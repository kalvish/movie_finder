package com.kalann.moviefinder.movies

import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.dagger.ApplicationScope
import com.kalann.moviefinder.db.MovieDatabase
import javax.inject.Inject

@ApplicationScope
class MovieManager @Inject constructor(private val mFinService: MFinService
                                       ,private val movieDatabase: MovieDatabase){
    suspend fun getMovieFromApi(id: Int) = mFinService.getMovieFromApi(id,MFinService.Instance.API_KEY)

    suspend fun getForType(type: MFinService.PageTypes,
                           page: Int) = mFinService.getMoviesFromApi(type.value,page,MFinService.Instance.API_KEY)

    suspend fun searchMovies(query: String,
                             page: Int) = mFinService.searchMovies(query,page,MFinService.Instance.API_KEY)

    fun saveMovieToDb(movie: Movie) = movieDatabase.movieDao().insert(movie)

    fun getMovieForIdFromDb(id: Int) = movieDatabase.movieDao().getMovieForId(id)

    fun deleteMovieFromDb(id: Int) = movieDatabase.movieDao().deleteMovie(id)

    suspend fun getMoviesAll(limit: Int, offset: Int) = movieDatabase.movieDao().getMoviesAll(limit, offset)

    fun getMoviesAllFlow() = movieDatabase.movieDao().getMoviesAllFlow()
}
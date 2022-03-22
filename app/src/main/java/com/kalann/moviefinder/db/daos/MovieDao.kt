package com.kalann.moviefinder.db.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kalann.moviefinder.api.moshi.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Query("SELECT * from Movie WHERE id = :id")
    fun getMovieForId(id: Int): Movie?

    @Query("DELETE from Movie WHERE id = :id")
    fun deleteMovie(id: Int)

    @Query("SELECT * from Movie limit :pageSize offset :offset")
    suspend fun getMoviesAll(pageSize: Int, offset: Int): List<Movie>

    @Query("SELECT * from Movie")
    fun getMoviesAllFlow(): Flow<List<Movie>>
}
package com.kalann.moviefinder.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kalann.moviefinder.api.moshi.Genre
import com.kalann.moviefinder.api.moshi.Movie

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: Genre)

    @Query("SELECT * from Genre WHERE id IN (:ids)")
    suspend fun getGenres(ids: List<Int>) : List<Genre>
}
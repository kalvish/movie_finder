package com.kalann.moviefinder.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.kalann.moviefinder.api.moshi.Genre
import com.kalann.moviefinder.api.moshi.Movie

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: Genre)
}
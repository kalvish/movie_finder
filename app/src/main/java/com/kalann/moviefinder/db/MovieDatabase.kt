package com.kalann.moviefinder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kalann.moviefinder.api.moshi.Genre
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.db.daos.GenreDao
import com.kalann.moviefinder.db.daos.MovieDao
import com.kalann.moviefinder.db.entities.typeconverters.GenreConverter
import com.kalann.moviefinder.db.entities.typeconverters.ProductionCompanyConverter
import com.kalann.moviefinder.db.entities.typeconverters.ProductionCountryConverter
import com.kalann.moviefinder.db.entities.typeconverters.SpokenLanguageConverter

@Database(entities = [Movie::class, Genre::class], version = 2, exportSchema = true)
@TypeConverters(GenreConverter::class, ProductionCompanyConverter::class, ProductionCountryConverter::class
    , SpokenLanguageConverter::class)
public abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    object Instance{
        val MOVIEDB_STARTING_PAGE_INDEX = 0
    }
}
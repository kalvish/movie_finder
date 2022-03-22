package com.kalann.moviefinder.dagger

import android.content.Context
import androidx.room.Room
import com.kalann.moviefinder.db.MovieDatabase
import com.kalann.moviefinder.db.MovieDatabaseMigrations
import com.kalann.moviefinder.db.daos.MovieDao
import dagger.Module
import dagger.Provides

@Module
class StorageModule {
    @ApplicationScope // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideMovieDatabase(app: Context) : MovieDatabase{
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "moviedb.db"
        ).addMigrations(MovieDatabaseMigrations.MIGRATION_1_2).build()
    }

    @ApplicationScope
    @Provides
    fun provideMovieDao(db: MovieDatabase) : MovieDao{
        return db.movieDao()
    }
}
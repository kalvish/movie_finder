package com.kalann.moviefinder.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MovieDatabaseMigrations {
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE \"Genre\" (\"id\" INTEGER PRIMARY KEY  NOT NULL, \"name\" TEXT)")
        }
    }
}
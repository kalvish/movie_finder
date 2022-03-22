package com.kalann.moviefinder

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kalann.moviefinder.db.MovieDatabase
import com.kalann.moviefinder.db.MovieDatabaseMigrations
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class MovieDatabaseMigrationTest {
    private val TEST_DB = "migration-test"

    @get:Rule
    var helper: MigrationTestHelper? = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MovieDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

//    fun migrationTest() {
//        helper = MigrationTestHelper(
//            InstrumentationRegistry.getInstrumentation(),
//            MovieDatabase::class.java.getCanonicalName(),
//            FrameworkSQLiteOpenHelperFactory()
//        )
//    }

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        // Create earliest version of the database.
        val db = helper!!.createDatabase(TEST_DB, 1)
        db.close()

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        val appDb: MovieDatabase = Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            MovieDatabase::class.java,
            TEST_DB
        )
            .addMigrations(*ALL_MIGRATIONS).build()
        appDb.getOpenHelper().getWritableDatabase()
        appDb.close()
    }

    // Array of all migrations
    private val ALL_MIGRATIONS = arrayOf(
        MovieDatabaseMigrations.MIGRATION_1_2
    )
}
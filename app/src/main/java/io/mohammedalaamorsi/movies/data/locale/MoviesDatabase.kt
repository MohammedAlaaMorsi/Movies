package io.mohammedalaamorsi.movies.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.mohammedalaamorsi.movies.data.models.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = MoviesDatabase.DATABASE_VERSION,
    exportSchema = false
)
abstract class MoviesDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        private const val DATABASE_NAME = "movies.db"
        const val DATABASE_VERSION = 1

        @Volatile
        private var INSTANCE: MoviesDatabase? = null

        fun getInstance(context: Context): MoviesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, MoviesDatabase::class.java, DATABASE_NAME).build()
    }
}

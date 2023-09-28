package com.lfd.mymovies.data.database

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

import java.time.LocalDateTime

@Database(entities = [DatabasePopularMovies::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
}

private lateinit var INSTANCE: MoviesDatabase

fun getDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(
                        context.applicationContext,
                        MoviesDatabase::class.java,
                        "movie"
                ).fallbackToDestructiveMigration()
                    .build()
        }
    }
    return INSTANCE
}

class DateConverters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
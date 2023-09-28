package com.lfd.mymovies.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabasePopularMovies)

    @Query("select * from DatabasePopularMovies order by releaseDate desc")
    fun getPopularMovies(): LiveData<List<DatabasePopularMovies>>

    @Query("SELECT * FROM DatabasePopularMovies WHERE title LIKE :searchQuery")
    fun getMoviesByTitle(searchQuery: String): LiveData<List<DatabasePopularMovies>>

    @Query("DELETE from DatabasePopularMovies")
    fun deleteOldPopularMovies()


}
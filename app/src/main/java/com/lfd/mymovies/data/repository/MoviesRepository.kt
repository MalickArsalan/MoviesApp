package com.lfd.mymovies.data.repository

import android.graphics.Movie
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.lfd.mymovies.data.api.Network
import com.lfd.mymovies.data.api.NetworkMovieContainer
import com.lfd.mymovies.data.api.asDatabaseModel
import com.lfd.mymovies.data.api.utils.parseMoviesJsonResult
import com.lfd.mymovies.data.database.MoviesDatabase
import com.lfd.mymovies.data.database.asDomainModel
import com.lfd.mymovies.data.domain.DomainMovie
import com.lfd.mymovies.utils.Constants.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class MoviesRepository(private val database: MoviesDatabase) {

    val movie: LiveData<List<DomainMovie>> =
        Transformations.map(database.moviesDao.getPopularMovies()) {
            it.asDomainModel()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchPopularMovies(page: Int = 1) {
        withContext(Dispatchers.IO) {
            try {
                val call =
                    Network.moviesNetwork.fetchPopularMovies(page, API_KEY).await()
//                val networkPopularMovies = call.await()
                /*                val movies = NetworkMovieContainer(call.networkMovies)

                                database.moviesDao.insertAll(*movies.asDatabaseModel())*/
            } catch (e: Exception) {
                Timber.e(e.message)
                Timber.e(e.stackTraceToString())
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            try {

                val call =
                    Network.moviesNetwork.fetchPopularMovies(1, API_KEY).await()
                if(call.isNotEmpty()) {
                    val movies = NetworkMovieContainer(parseMoviesJsonResult(JSONObject(call)))
                    database.moviesDao.deleteOldPopularMovies()
                    database.moviesDao.insertAll(*movies.asDatabaseModel())
                }
            } catch (e: Exception) {
                Timber.e(e.message)
                Timber.e(e.stackTraceToString())
                e.printStackTrace()
            }
        }
    }

    fun searchMoviesByTitle(searchQuery: String): LiveData<List<DomainMovie>> {
        val searchFilter = "%$searchQuery%"
       val searchResult = Transformations.map(database.moviesDao.getMoviesByTitle(searchFilter)) {
            it.asDomainModel()
        }
        return searchResult
    }

}
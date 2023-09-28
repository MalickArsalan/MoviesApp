package com.lfd.mymovies.data.api


import android.os.Build
import androidx.annotation.RequiresApi
import com.lfd.mymovies.data.database.DatabasePopularMovies
import com.lfd.mymovies.data.domain.DomainMovie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@JsonClass(generateAdapter = true)
data class NetworkMovieContainer(val movies: ArrayList<NetworkMovie>)

@JsonClass(generateAdapter = true)
data class NetworkPopularMovies(
    val page: Int,
    @Json(name = "results")
    val networkMovies: ArrayList<NetworkMovie>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int,
)

@JsonClass(generateAdapter = true)
data class NetworkMovie(

    @Json(name = "backdrop_path")
    val backdropPath: String,
    val id: Long,
    val overview: String,
    @Json(name = "poster_path")
    val posterPath: String,
    @Json(name = "release_date")
    val releaseDate: String,
    val title: String,
    @Json(name = "vote_average")
    val rating: Double,
)


/*data class NetworkMovie(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val posterPath: String,
    @Json(name = "backdrop_path") val backdropPath: String,
    @Json(name = "vote_average") val rating: Double,
    @Json(name = "release_date") val releaseDate: String,
)

data class GetMoviesResponse(
    @Json(name = "page") val page: Int,
    @Json(name = "networkMovies") val networkMovies: List<NetworkMovie>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int,
)*/

fun NetworkMovieContainer.asDomainModel(): List<DomainMovie> {
    return movies.map {
        DomainMovie(
                id = it.id,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath,
                rating = it.rating,
                releaseDate = it.releaseDate
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NetworkMovieContainer.asDatabaseModel(): Array<DatabasePopularMovies> {

    return movies.map {
        DatabasePopularMovies(
                id = it.id,
                title = it.title,
                overview = it.overview,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath,
                rating = it.rating,
                releaseDate =
                it.releaseDate,


                )
    }.toTypedArray()
}

package com.lfd.mymovies.data.database


import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lfd.mymovies.data.domain.DomainMovie
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Entity
@Parcelize
data class DatabasePopularMovies constructor(
    @PrimaryKey
    var id: Long,
    var title: String,
    var overview: String,
    val posterPath: String,
    val backdropPath: String,
    var rating: Double,
    var releaseDate: String,
): Parcelable {

    val createdDateFormatted : String
        @RequiresApi(Build.VERSION_CODES.O)
        get() =releaseDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

}

fun List<DatabasePopularMovies>.asDomainModel(): List<DomainMovie> {
    return map{
        DomainMovie(
                id=it.id,
                title = it.title,
                overview =it.overview,
                posterPath = it.posterPath,
                backdropPath = it.backdropPath,
                rating = it.rating,
                releaseDate = it.releaseDate
        )
    }
}



package com.lfd.mymovies.data.domain

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class DomainMoviesContainer(val movies: ArrayList<DomainMovie>)

@Parcelize
data class DomainMovie(
    val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val rating: Double,
    var releaseDate: String,
) : Parcelable



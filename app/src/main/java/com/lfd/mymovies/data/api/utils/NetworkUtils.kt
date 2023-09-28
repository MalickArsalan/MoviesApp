package com.lfd.mymovies.data.api.utils

import com.lfd.mymovies.data.api.NetworkMovie
import org.json.JSONObject
import java.util.ArrayList

fun parseMoviesJsonResult(jsonResult: JSONObject): ArrayList<NetworkMovie> {

    val reaults = jsonResult.getJSONArray("results")

    val movieList = ArrayList<NetworkMovie>()

    for (i in 0 until reaults.length()) {
        val movieJson = reaults.getJSONObject(i)

        val movie = NetworkMovie(
                movieJson.getString("backdrop_path"),
                movieJson.getLong("id"),
                movieJson.getString("overview"),
                movieJson.getString(
                        "poster_path"
                ),
                movieJson.getString("release_date"),
                movieJson.getString("title"),
                movieJson.getDouble("vote_average"),
        )
        movieList.add(movie)
    }

    return movieList
}

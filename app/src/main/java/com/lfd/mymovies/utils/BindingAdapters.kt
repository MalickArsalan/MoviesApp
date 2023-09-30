package com.lfd.mymovies.utils

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.lfd.mymovies.R
import com.lfd.mymovies.data.api.utils.ConnectivityObserver
import com.lfd.mymovies.data.domain.DomainMovie
import com.lfd.mymovies.ui.overview.MoviesGridAdapter


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .transform(CenterCrop())
            .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
            )
            .into(imgView)

    }
}

@BindingAdapter("rating")
fun ratingCalculation(ratingBar: RatingBar, ratingAverage: Double) {
    ratingBar.rating = ratingAverage.toFloat() / 2
}

@BindingAdapter(value = ["movieList", "statusTextError"], requireAll = false)
fun showNetworkError(
    textView: TextView,
    movieList: List<DomainMovie>?,
    status: ConnectivityObserver.Status,
) {
    textView.visibility = if (!movieList.isNullOrEmpty()) {
        View.GONE
    } else {
        if (status == ConnectivityObserver.Status.Available) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}

@BindingAdapter(value = ["movieList", "statusProgressBar"], requireAll = false)
fun goneIfNotNull(view: View, movieList: List<DomainMovie>?, status: ConnectivityObserver.Status) {
    view.visibility = if (!movieList.isNullOrEmpty()) {
        View.GONE
    } else {
        if (status == ConnectivityObserver.Status.Available) {
            View.VISIBLE
        } else {
            View.GONE
        }

    }
}


@BindingAdapter(value = ["listData", "searchChar"], requireAll = false)
fun bindRecyclerView(recyclerView: RecyclerView, data: List<DomainMovie>?, searchChar: String) {
    val adapter = recyclerView.adapter as MoviesGridAdapter
//    adapter.originalList = data!!
    if (!data.isNullOrEmpty()) {
        adapter.originalList = data.toList()
    }
    if (searchChar.isEmpty())
        adapter.submitList(data)
    else
        adapter.filter.filter(searchChar)
}
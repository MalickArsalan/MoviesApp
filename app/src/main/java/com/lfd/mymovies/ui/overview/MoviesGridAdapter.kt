package com.lfd.mymovies.ui.overview

import android.graphics.Movie
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filter.FilterListener
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.lfd.mymovies.data.domain.DomainMovie
import com.lfd.mymovies.databinding.MovieItemBinding
import java.util.Locale

class MoviesGridAdapter(private val callback: MovieClick) :
    ListAdapter<DomainMovie, MoviesGridAdapter.MovieListViewHolder>(DiffCallback), Filterable {

     var originalList: List<DomainMovie> = emptyList() // Store the original unfiltered list

    companion object DiffCallback : DiffUtil.ItemCallback<DomainMovie>() {
        override fun areItemsTheSame(oldItem: DomainMovie, newItem: DomainMovie): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DomainMovie, newItem: DomainMovie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieListViewHolder(private var binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: MovieClick, movie: DomainMovie) {
            binding.movie = movie
            binding.movieCallback = listener
        }

        companion object {
            fun from(parent: ViewGroup): MovieListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
                return MovieListViewHolder(binding)
            }
        }
    }

    class MovieClick(val clickListener: (movie: DomainMovie) -> Unit) {
        fun onClick(domainMovie: DomainMovie) = clickListener(domainMovie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(callback, getItem(position))
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = if (constraint.isNullOrEmpty()) {
                // If the constraint is empty or null, return the original list
                originalList.toMutableList()
            }else{
                // Filter the list based on the constraint (search query)
                val query = constraint.toString().lowercase(Locale.ROOT).trim()
                originalList.filter { movie ->
                    movie.title.lowercase(Locale.ROOT).contains(query)
                }.toMutableList()
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            // Update the adapter's data with the filtered list
            results?.values?.let { filteredList ->
                submitList(filteredList as List<DomainMovie>)
            }
        }

    }

}



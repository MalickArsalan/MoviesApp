package com.lfd.mymovies.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lfd.mymovies.R
import com.lfd.mymovies.databinding.FragmentMovieDetailBinding
import com.lfd.mymovies.databinding.FragmentOverviewBinding


class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentMovieDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val movie = MovieDetailFragmentArgs.fromBundle(requireArguments()).selectedMovie

        binding.movie = movie

        return binding.root
    }

    companion object {

    }
}
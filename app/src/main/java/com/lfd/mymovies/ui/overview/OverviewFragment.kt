package com.lfd.mymovies.ui.overview

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lfd.mymovies.data.api.utils.ConnectivityObserver
import com.lfd.mymovies.databinding.FragmentOverviewBinding
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.O)
class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding


    private val viewModel: OverviewViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
                this,
                OverviewViewModel.Factory(
                        activity.application,

                        )
        ).get(OverviewViewModel::class.java)
    }

    private var viewModelAdapter: MoviesGridAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigateToSelectedMovies.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController().navigate(
                        OverviewFragmentDirections.actionOverviewFragmentToMovieDetailFragment(it)
                )
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        // Observe network status changes within the ViewModel
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.networkStatus.collect { status ->
                when (status) {
                    ConnectivityObserver.Status.Available -> {
                        Timber.d("Available")
                        viewModel.refreshMoviesOnNetwrokAvailability()
                    }

                    ConnectivityObserver.Status.Unavailable -> {
                        Timber.d("Unavailable")
                    }

                    ConnectivityObserver.Status.Losing -> {
                        Timber.d("Losing")
                    }

                    ConnectivityObserver.Status.Lost -> {
                        Timber.d("Lost")
                    }
                }
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOverviewBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.moviesGrid.apply {
            viewModelAdapter = MoviesGridAdapter(MoviesGridAdapter.MovieClick {
                viewModel.displayMovieDetails(it)
            })

            adapter = viewModelAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        binding.searchMoview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submission if needed
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Update the ViewModel with the new query
                if(viewModelAdapter!!.originalList.isEmpty()) {
                    viewModelAdapter!!.originalList = viewModel.movieList.value!!
                }
                viewModelAdapter!!.filter.filter(newText)
                return true
            }
        })


        return binding.root
    }

    companion object
}
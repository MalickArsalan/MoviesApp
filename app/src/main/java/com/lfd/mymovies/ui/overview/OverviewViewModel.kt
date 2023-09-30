package com.lfd.mymovies.ui.overview

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lfd.mymovies.data.api.utils.ConnectivityObserver
import com.lfd.mymovies.data.api.utils.NetworkConnectivityObserver
import com.lfd.mymovies.data.database.getDatabase
import com.lfd.mymovies.data.domain.DomainMovie
import com.lfd.mymovies.data.repository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val moviesRepository = MoviesRepository(database)
    private val connectivityObserver = NetworkConnectivityObserver(application.applicationContext)

    val movieList = moviesRepository.movie

    private val _filteredMovies = MutableLiveData<List<DomainMovie?>>()
    val filteredMovies: LiveData<List<DomainMovie?>>
        get() = _filteredMovies

    val searchChar: MutableLiveData<String> = MutableLiveData()

    var initialQuery: String = ""

    private val _networkStatus =
        MutableStateFlow<ConnectivityObserver.Status?>(ConnectivityObserver.Status.Unavailable)
    val networkStatus: StateFlow<ConnectivityObserver.Status?>
        get() = _networkStatus

    private val _navigateToSelectedDomainMovie = MutableLiveData<DomainMovie?>()
    val navigateToSelectedMovies: LiveData<DomainMovie?>
        get() = _navigateToSelectedDomainMovie

    fun displayMovieDetails(movie: DomainMovie) {
        _navigateToSelectedDomainMovie.value = movie
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedDomainMovie.value = null
    }

    init {
        startObservingNetworkState()
        searchChar.value = ""
        /*viewModelScope.launch {
            try {
                moviesRepository.refreshMovies()
            } catch (e: Exception) {
                Timber.e(e.message)
            }
        }*/
    }

    fun refreshMoviesOnNetworkAvailability() {
        if (movieList.value.isNullOrEmpty()) {
            viewModelScope.launch {
                moviesRepository.refreshMovies()
            }
        }
    }

    private fun startObservingNetworkState() {
        viewModelScope.launch {
            connectivityObserver.observer()
                .distinctUntilChanged()
                .collect { status ->
                    _networkStatus.value = status
                }
        }
    }

    fun updateQuery(query: String?) {

        val filteredQuery = query ?: initialQuery
        if(!filteredQuery.isNullOrEmpty()) {
            viewModelScope.launch {
                val filteredList = moviesRepository.searchMoviesByTitle(filteredQuery)
                _filteredMovies.value = filteredList.value
            }
        }else{
            _filteredMovies.value = movieList.value
        }

    }

    fun isNetworkAvailable(): Boolean {
        return _networkStatus.value == ConnectivityObserver.Status.Available
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
                return OverviewViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
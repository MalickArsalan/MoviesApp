package com.lfd.mymovies.utils

import com.lfd.mymovies.data.api.utils.ConnectivityObserver
import com.lfd.mymovies.data.domain.DomainMovie

data class CustomParameters(val list: List<DomainMovie>, val networkStatus: ConnectivityObserver.Status)
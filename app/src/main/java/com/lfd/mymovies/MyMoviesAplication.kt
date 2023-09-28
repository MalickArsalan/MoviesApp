package com.lfd.mymovies

import android.app.Application
import com.squareup.picasso.Picasso
import timber.log.Timber

class MyMoviesAplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        val picasso = Picasso.get()
        picasso.setIndicatorsEnabled(true)
        picasso.isLoggingEnabled = true
    }
}
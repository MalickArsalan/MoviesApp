package com.lfd.mymovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.lfd.mymovies.R
import com.lfd.mymovies.data.api.utils.ConnectivityObserver
import com.lfd.mymovies.data.api.utils.NetworkConnectivityObserver
import com.lfd.mymovies.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.title = "Popular Movies"

    }
}
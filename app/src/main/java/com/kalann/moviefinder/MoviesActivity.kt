package com.kalann.moviefinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.kalann.moviefinder.dagger.MoviesComponent
import com.kalann.moviefinder.databinding.ActivityMoviesBinding

class MoviesActivity : AppCompatActivity() {
    lateinit var moviesComponent: MoviesComponent
    lateinit var activityMoviesBinding: ActivityMoviesBinding
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        moviesComponent = (applicationContext as MFinApplication)
            .daggerAppComponent.moviesComponent().create()
        moviesComponent.inject(this)
        super.onCreate(savedInstanceState)
        activityMoviesBinding = ActivityMoviesBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_movies)
        setContentView(activityMoviesBinding.root)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_now_playing,
            R.id.navigation_popular,
            R.id.navigation_top_rated,
            R.id.navigation_upcoming
        )
            .build()
        navController = findNavController(this, R.id.nav_host_fragment_activity_movies)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(activityMoviesBinding.navView, navController)
    }
}
package com.kalann.moviefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kalann.moviefinder.dagger.DaggerAppComponent
import com.kalann.moviefinder.dagger.MoviesComponent

class MoviesActivity : AppCompatActivity() {
    lateinit var moviesComponent: MoviesComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        moviesComponent = (applicationContext as MFinApplication)
            .daggerAppComponent.moviesComponent().create()
        moviesComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)
    }
}
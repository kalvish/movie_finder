package com.kalann.moviefinder.dagger

import com.kalann.moviefinder.MoviesActivity
import dagger.Subcomponent

@MoviesScope
@Subcomponent
interface MoviesComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MoviesComponent
    }

    fun inject(moviesActivity: MoviesActivity)
}
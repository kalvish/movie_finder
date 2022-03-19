package com.kalann.moviefinder.dagger

import dagger.Component

@ApplicationScope
@Component(modules = [NetworkModule::class, SubcomponentsModule::class])
interface AppComponent {
    fun moviesComponent() : MoviesComponent.Factory
}
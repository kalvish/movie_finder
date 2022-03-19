package com.kalann.moviefinder.dagger

import android.content.Context
import com.kalann.moviefinder.movies.MovieManager
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [NetworkModule::class, SubcomponentsModule::class])
interface AppComponent {
    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun moviesComponent() : MoviesComponent.Factory
    fun movieManager(): MovieManager
}
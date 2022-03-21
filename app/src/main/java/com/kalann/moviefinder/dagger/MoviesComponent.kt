package com.kalann.moviefinder.dagger

import com.kalann.moviefinder.MoviesActivity
import com.kalann.moviefinder.ui.movie_details.MovieDetailsFragment
import com.kalann.moviefinder.ui.search.SearchResultsActivity
import com.kalann.moviefinder.ui.now_playing.NowPlayingFragment
import com.kalann.moviefinder.ui.popular.PopularFragment
import com.kalann.moviefinder.ui.top_rated.TopRatedFragment
import com.kalann.moviefinder.ui.upcoming.UpcomingFragment
import dagger.Subcomponent

@MoviesScope
@Subcomponent
interface MoviesComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MoviesComponent
    }

    fun inject(moviesActivity: MoviesActivity)
    fun inject(nowPlayingFragment: NowPlayingFragment)
    fun inject(popularFragment: PopularFragment)
    fun inject(topRatedFragment: TopRatedFragment)
    fun inject(upcomingFragment: UpcomingFragment)

    fun inject(searchResultsActivity: SearchResultsActivity)

    fun inject(movieDetailsFragment: MovieDetailsFragment)
}
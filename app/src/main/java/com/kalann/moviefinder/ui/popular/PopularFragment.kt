package com.kalann.moviefinder.ui.popular

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kalann.moviefinder.MFinApplication
import com.kalann.moviefinder.MoviesActivity
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.dagger.MoviesComponent
import com.kalann.moviefinder.databinding.FragmentPopularBinding
import com.kalann.moviefinder.movies.MovieAdapter
import com.kalann.moviefinder.movies.MovieDataRepository
import com.kalann.moviefinder.ui.now_playing.NowPlayingViewModel
import com.kalann.moviefinder.ui.now_playing.NowPlayingViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PopularFragment: Fragment() {
    lateinit var fragmentPopularBinding: FragmentPopularBinding
    lateinit var rootView: View
    @Inject
    lateinit var moviesDataRepository: MovieDataRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPopularBinding = FragmentPopularBinding.inflate(inflater, container, false)
        rootView = fragmentPopularBinding.root
        return rootView
//        return inflater.inflate(R.layout.fragment_popular, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MoviesActivity).moviesComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val popularViewModelFactory = PopularViewModelFactory(moviesDataRepository, MFinService.PageTypes.POPULAR)
        val popularViewModel = ViewModelProvider(this, popularViewModelFactory)
            .get(PopularViewModel::class.java)
        val movieAdapter = MovieAdapter(object : MovieAdapter.OnMovieClickListener {
            override fun onClickListItem(movie: Movie) {
            }
        })
        fragmentPopularBinding.recyclerViewMovies.adapter = movieAdapter
        lifecycleScope.launch {
            popularViewModel.pagingDataFlow.collectLatest(movieAdapter::submitData)
        }
    }
}
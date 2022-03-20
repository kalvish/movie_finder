package com.kalann.moviefinder.ui.upcoming

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kalann.moviefinder.MoviesActivity
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.databinding.FragmentTopRatedBinding
import com.kalann.moviefinder.movies.MovieAdapter
import com.kalann.moviefinder.movies.MovieDataRepository
import com.kalann.moviefinder.ui.popular.PopularViewModel
import com.kalann.moviefinder.ui.popular.PopularViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpcomingFragment: Fragment() {
    lateinit var fragmentTopRatedBinding: FragmentTopRatedBinding
    lateinit var rootView: View
    @Inject
    lateinit var moviesDataRepository: MovieDataRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentTopRatedBinding = FragmentTopRatedBinding.inflate(inflater, container, false)
        rootView = fragmentTopRatedBinding.root
        return rootView
//        return inflater.inflate(R.layout.fragment_top_rated, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MoviesActivity).moviesComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val popularViewModelFactory = PopularViewModelFactory(moviesDataRepository, MFinService.PageTypes.UPCOMING)
        val popularViewModel = ViewModelProvider(this, popularViewModelFactory)
            .get(PopularViewModel::class.java)
        val movieAdapter = MovieAdapter(object : MovieAdapter.OnMovieClickListener {
            override fun onClickListItem(movie: Movie) {
            }
        })
        fragmentTopRatedBinding.recyclerViewMovies.adapter = movieAdapter
        lifecycleScope.launch {
            popularViewModel.pagingDataFlow.collectLatest(movieAdapter::submitData)
        }
    }
}
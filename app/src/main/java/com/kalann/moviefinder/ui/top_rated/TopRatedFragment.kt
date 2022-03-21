package com.kalann.moviefinder.ui.top_rated

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.kalann.moviefinder.MoviesActivity
import com.kalann.moviefinder.R
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.databinding.FragmentTopRatedBinding
import com.kalann.moviefinder.movies.MovieAdapter
import com.kalann.moviefinder.movies.MovieConstants
import com.kalann.moviefinder.movies.MovieDataRepository
import com.kalann.moviefinder.ui.MoviesViewModelLifecycle
import com.kalann.moviefinder.ui.MoviesViewModelLifecycleFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopRatedFragment: Fragment() {
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
        val popularViewModelFactory = MoviesViewModelLifecycleFactory(moviesDataRepository, MFinService.PageTypes.TOP_RATED)
        val popularViewModel = ViewModelProvider(this, popularViewModelFactory)
            .get(MoviesViewModelLifecycle::class.java)
        val movieAdapter = MovieAdapter(object : MovieAdapter.OnMovieClickListener {
            override fun onClickListItem(movie: Movie) {
                val bundle = bundleOf(MovieConstants.MOVIE_ID to movie.id)
                view.findNavController().navigate(R.id.action_navigation_top_rated_to_movieDetailsFragment, bundle)
            }
        })
        fragmentTopRatedBinding.recyclerViewMovies.adapter = movieAdapter
        lifecycleScope.launch {
            popularViewModel.pagingDataFlow.collectLatest(movieAdapter::submitData)
        }
    }
}
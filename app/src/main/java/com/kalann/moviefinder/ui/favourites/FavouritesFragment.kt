package com.kalann.moviefinder.ui.favourites

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
import androidx.recyclerview.widget.DividerItemDecoration
import com.kalann.moviefinder.MoviesActivity
import com.kalann.moviefinder.R
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.databinding.FragmentFavouritesBinding
import com.kalann.moviefinder.databinding.FragmentPopularBinding
import com.kalann.moviefinder.movies.MovieAdapter
import com.kalann.moviefinder.movies.MovieConstants
import com.kalann.moviefinder.movies.MovieDataRepository
import com.kalann.moviefinder.ui.MoviesViewModelLifecycle
import com.kalann.moviefinder.ui.MoviesViewModelLifecycleFactory
import com.kalann.moviefinder.ui.now_playing.NwPlayingMoviesAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesFragment : Fragment() {
    lateinit var fragmentFavouritesBinding: FragmentFavouritesBinding
    lateinit var rootView: View
    lateinit var popularViewModel: MoviesViewModelLifecycle
    lateinit var movieAdapter: MovieAdapter
    @Inject
    lateinit var moviesDataRepository: MovieDataRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentFavouritesBinding = FragmentFavouritesBinding.inflate(inflater, container, false)
        rootView = fragmentFavouritesBinding.root
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MoviesActivity).moviesComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val popularViewModelFactory = MoviesViewModelLifecycleFactory(moviesDataRepository, MFinService.PageTypes.POPULAR)
        popularViewModel = ViewModelProvider(this, popularViewModelFactory)
            .get(MoviesViewModelLifecycle::class.java)
//        movieAdapter = MovieAdapter(object : MovieAdapter.OnMovieClickListener {
//            override fun onClickListItem(movie: Movie) {
//                val bundle = bundleOf(MovieConstants.MOVIE_ID to movie.id)
//                view.findNavController().navigate(R.id.action_favouritesFragment_to_movieDetailsFragment, bundle)
//            }
//        })
//        fragmentFavouritesBinding.recyclerViewMovies.adapter = movieAdapter
        val adapterMovies = NwPlayingMoviesAdapter(object : NwPlayingMoviesAdapter.OnMovieClickListener {
            override fun onClickListItem(movie: Movie) {
                val bundle = bundleOf(MovieConstants.MOVIE_ID to movie.id)
                view.findNavController().navigate(R.id.action_favouritesFragment_to_movieDetailsFragment, bundle)
            }
        })
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        fragmentFavouritesBinding.recyclerViewMovies.addItemDecoration(decoration)
        fragmentFavouritesBinding.recyclerViewMovies.adapter = adapterMovies
        lifecycleScope.launch {
//            popularViewModel.pagingDataFlowDb.collect {
//                movieAdapter.submitData(it)
//            }
            popularViewModel.getMoviesAllFlow().collect {
                adapterMovies.submitList(it)
            }
        }
    }
}
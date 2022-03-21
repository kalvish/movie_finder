package com.kalann.moviefinder.ui.now_playing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kalann.moviefinder.MFinApplication
import com.kalann.moviefinder.MoviesActivity
import com.kalann.moviefinder.R
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.dagger.MoviesComponent
import com.kalann.moviefinder.databinding.FragmentNowPlayingBinding
import com.kalann.moviefinder.movies.MovieAdapter
import com.kalann.moviefinder.movies.MovieConstants
import com.kalann.moviefinder.movies.MovieDataRepository
import com.kalann.moviefinder.movies.MoviesViewModel
import com.kalann.moviefinder.ui.MoviesViewModelLifecycle
import com.kalann.moviefinder.ui.MoviesViewModelLifecycleFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class NowPlayingFragment: Fragment() {
    lateinit var fragmentNowPlayingBinding: FragmentNowPlayingBinding
    lateinit var rootView: View

    @Inject
    lateinit var moviesDataRepository: MovieDataRepository
//    @Inject
//    lateinit var moviesViewModel: MoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNowPlayingBinding = FragmentNowPlayingBinding.inflate(inflater, container, false)
        rootView = fragmentNowPlayingBinding.root
        return rootView
//        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MoviesActivity).moviesComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val popularViewModelFactory = MoviesViewModelLifecycleFactory(moviesDataRepository, MFinService.PageTypes.NOW_PLAYING)
        val popularViewModel = ViewModelProvider(this, popularViewModelFactory)
            .get(MoviesViewModelLifecycle::class.java)
        val movieAdapter = MovieAdapter(object : MovieAdapter.OnMovieClickListener {
            override fun onClickListItem(movie: Movie) {
                val bundle = bundleOf(MovieConstants.MOVIE_ID to movie.id)
                view.findNavController().navigate(R.id.action_navigation_now_playing_to_movieDetailsFragment, bundle)
            }
        })
        fragmentNowPlayingBinding.recyclerViewMovies.adapter = movieAdapter
        lifecycleScope.launch {
            popularViewModel.pagingDataFlow.collectLatest(movieAdapter::submitData)
        }

//        val nowPlayingViewModelFactory = NowPlayingViewModelFactory(moviesViewModel)
//        val nowPlayingViewModel = ViewModelProvider(this, nowPlayingViewModelFactory)
//            .get(NowPlayingViewModel::class.java)
//
//        nowPlayingViewModel.networkStatus.observe(viewLifecycleOwner) {
//            val imageViewNetworkStatus = view.findViewById<ImageView>(R.id.imageview_network_status)
//            when (it) {
//                MFinService.MovieApiNetworkStatus.LOADING -> {
//                    imageViewNetworkStatus.visibility = View.VISIBLE
//                    imageViewNetworkStatus.setImageResource(R.drawable.loading_animation)
//                }
//                MFinService.MovieApiNetworkStatus.ERROR -> {
//                    imageViewNetworkStatus.visibility = View.VISIBLE
//                    imageViewNetworkStatus.setImageResource(R.drawable.ic_connection_error)
//                }
//                MFinService.MovieApiNetworkStatus.SUCCESS -> imageViewNetworkStatus.visibility =
//                    View.GONE
//                else -> imageViewNetworkStatus.visibility = View.GONE
//            }
//        }
//
//        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_movies)
//        val adapterMovies = NwPlayingMoviesAdapter(object : NwPlayingMoviesAdapter.OnMovieClickListener {
//            override fun onClickListItem(movie: Movie) {
//            }
//
//        })
//        recyclerView.adapter = adapterMovies
//        nowPlayingViewModel.getMoviesForType(MFinService.PageTypes.NOW_PLAYING,2).observe(viewLifecycleOwner, {
//            adapterMovies.submitList(it)
//        })
    }
}
package com.kalann.moviefinder.ui.movie_details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kalann.moviefinder.MoviesActivity
import com.kalann.moviefinder.R
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import com.kalann.moviefinder.databinding.FragmentMovieDetailBinding
import com.kalann.moviefinder.movies.MovieConstants
import com.kalann.moviefinder.movies.MovieDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieDetailsFragment : Fragment() {
    lateinit var fragmentMovieDetailBinding: FragmentMovieDetailBinding
    lateinit var rootView: View
    @Inject
    lateinit var moviesDataRepository: MovieDataRepository
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMovieDetailBinding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        rootView = fragmentMovieDetailBinding.root
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MoviesActivity).moviesComponent.inject(this)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieDetailsViewModelFactory = MovieDetailsViewModelFactory(moviesDataRepository)
        val movieDetailsViewModel = ViewModelProvider(this, movieDetailsViewModelFactory)
            .get(MovieDetailsViewModel::class.java)

        movieDetailsViewModel.networkStatus.observe(viewLifecycleOwner) {
            val imageViewNetworkStatus = fragmentMovieDetailBinding.imageviewNetworkStatus
            when (it) {
                MFinService.MovieApiNetworkStatus.LOADING -> {
                    imageViewNetworkStatus.visibility = View.VISIBLE
                    imageViewNetworkStatus.setImageResource(R.drawable.loading_animation)
                }
                MFinService.MovieApiNetworkStatus.ERROR -> {
                    imageViewNetworkStatus.visibility = View.VISIBLE
                    imageViewNetworkStatus.setImageResource(R.drawable.ic_connection_error)
                }
                MFinService.MovieApiNetworkStatus.SUCCESS -> imageViewNetworkStatus.visibility =
                    View.GONE
                else -> imageViewNetworkStatus.visibility = View.GONE
            }
        }

        val movieId = arguments?.get(MovieConstants.MOVIE_ID) as Int
        var movieLoaded : Movie? = null
        movieDetailsViewModel.getMovie(movieId).observe(viewLifecycleOwner) { movie ->
            movieLoaded = movie
            bind(movie, fragmentMovieDetailBinding.imageViewMovieDetailImage)
            //If movie loaded successfully, user will get the ability to click
            //favourite icon
            fragmentMovieDetailBinding.imageViewFavourite.visibility = View.VISIBLE
        }

        fragmentMovieDetailBinding.imageViewFavourite.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                var movieFromDb: Movie?
                withContext(Dispatchers.IO) {
                    movieFromDb = movieDetailsViewModel.getMovieForIdFromDb(movieId)
                }
//                    withContext(Dispatchers.Main) {
                if (movieFromDb == null) {
                    withContext(Dispatchers.IO) {
                        movieDetailsViewModel.saveMovieToDb(movieLoaded!!)
                    }
                    fragmentMovieDetailBinding.imageViewFavourite.setImageResource(R.drawable.outline_favorite_24)
                } else {
                    withContext(Dispatchers.IO) {
                        movieDetailsViewModel.deleteMovieFromDb(movieFromDb!!)
                    }
                    fragmentMovieDetailBinding.imageViewFavourite.setImageResource(R.drawable.outline_favorite_border_24)
                }
//                    }
            }
        }

        lifecycleScope.launch {
            var movieFromDb: Movie? = null
            withContext(Dispatchers.IO) {
                movieFromDb = movieDetailsViewModel.getMovieForIdFromDb(movieId)
            }
            if (movieFromDb == null) {
                fragmentMovieDetailBinding.imageViewFavourite.setImageResource(R.drawable.outline_favorite_border_24)
            }else{
                fragmentMovieDetailBinding.imageViewFavourite.setImageResource(R.drawable.outline_favorite_24)
            }
        }
    }

    private fun bind(movie: Movie?, imageViewMovie: ImageView) {
        if (movie?.backdropPath == null) {
            imageViewMovie.setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.ic_broken_image, null))
        } else {
            val stringUrl = MFinService.Instance.IMAGE_BASE_URL + movie.backdropPath
            val uri = stringUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(requireActivity())
                .load(uri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image))
                .into(imageViewMovie)
        }
    }
}
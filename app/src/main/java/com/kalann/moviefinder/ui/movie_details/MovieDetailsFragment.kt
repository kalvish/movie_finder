package com.kalann.moviefinder.ui.movie_details

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.chip.Chip
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
import java.text.SimpleDateFormat
import java.util.*
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
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = movie?.originalTitle
            movieLoaded = movie
            bind(movie, fragmentMovieDetailBinding)
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
                    ImageViewCompat.setImageTintList(fragmentMovieDetailBinding.imageViewFavourite,
                        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.holo_orange_light)))
                } else {
                    withContext(Dispatchers.IO) {
                        movieDetailsViewModel.deleteMovieFromDb(movieFromDb!!)
                    }
                    fragmentMovieDetailBinding.imageViewFavourite.setImageResource(R.drawable.outline_favorite_border_24)
                    ImageViewCompat.setImageTintList(fragmentMovieDetailBinding.imageViewFavourite,
                        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.holo_orange_light)))
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
                ImageViewCompat.setImageTintList(fragmentMovieDetailBinding.imageViewFavourite,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.holo_orange_light)))

            }else{
                fragmentMovieDetailBinding.imageViewFavourite.setImageResource(R.drawable.outline_favorite_24)
                ImageViewCompat.setImageTintList(fragmentMovieDetailBinding.imageViewFavourite,
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.holo_orange_light)))
            }
        }
    }

    private fun bind(movie: Movie?, fragmentMovieDetailBindingTemp: FragmentMovieDetailBinding) {
        if (movie?.backdropPath == null) {
            fragmentMovieDetailBindingTemp.imageViewMovieDetailImage
                .setImageDrawable(ResourcesCompat.getDrawable(requireActivity().resources, R.drawable.ic_broken_image, null))
        } else {
            val stringUrl = MFinService.Instance.IMAGE_BASE_URL_W500 + movie.backdropPath
            val uri = stringUrl.toUri().buildUpon().scheme("https").build()
            fragmentMovieDetailBindingTemp.imageViewMovieDetailImage.load(stringUrl) {
                crossfade(true)
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
//            Glide.with(requireActivity())
//                .load(uri)
//                .apply(
//                    RequestOptions()
//                        .placeholder(R.drawable.loading_animation)
//                        .error(R.drawable.ic_broken_image))
//                .into(fragmentMovieDetailBindingTemp.imageViewMovieDetailImage)

            fragmentMovieDetailBindingTemp.textViewMovieName.text = movie?.originalTitle

            fragmentMovieDetailBindingTemp.chipGroupGenres.removeAllViews()
            movie?.genres?.forEach {
                val chip = Chip(requireActivity())
                chip.text = it.name
                fragmentMovieDetailBindingTemp.chipGroupGenres.addView(chip)
            }

            movie?.voteAverage?.let {
                fragmentMovieDetailBindingTemp.chipVoteDetails.visibility = View.VISIBLE
                fragmentMovieDetailBindingTemp.chipVoteDetails.text = resources.getString(R.string.vote_details,
                    movie!!.voteAverage, movie!!.voteCount)
                fragmentMovieDetailBindingTemp.chipVoteDetails.chipBackgroundColor =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), android.R.color.holo_orange_light))
            }

            movie?.releaseDate?.let {
                val formatFrom = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formatTo = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                try {
                    val stringDate = formatFrom.parse(it)
                    stringDate?.let {
                        val stringDateOutput = formatTo.format(it)
                        fragmentMovieDetailBindingTemp.textViewMovieReleaseDate.text = stringDateOutput
                    }
                } catch (e: Exception) {
                    Log.d("MovieViewHolder: " , "Release date: " + e.message)
                }
            }

            movie.overview.let {
                fragmentMovieDetailBindingTemp.textViewMovieDetails.text = it
            }

            movie.status.let {
                fragmentMovieDetailBindingTemp.textViewMovieReleaseStatus.text = it
            }

            movie.tagline.let {
                fragmentMovieDetailBindingTemp.textViewMovieReleaseTagline.text = it
            }
        }
    }
}
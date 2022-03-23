package com.kalann.moviefinder.movies

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kalann.moviefinder.R
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class MovieViewHolder(itemView: View, onMovieClickListener: MovieAdapter.OnMovieClickListener) : RecyclerView.ViewHolder(itemView) {
    val imageViewMovie = itemView.findViewById<ImageView>(R.id.imageViewMovie)
    val textViewMovieName = itemView.findViewById<AppCompatTextView>(R.id.textViewMovieName)
    val textViewMovieReleaseDate = itemView.findViewById<AppCompatTextView>(R.id.textViewMovieReleaseDate)
    val chipGroupGenres = itemView.findViewById<ChipGroup>(R.id.chipGroupGenres)
    val chipVoteDetails = itemView.findViewById<Chip>(R.id.chipVoteDetails)
//        val imageViewMovieType = itemView.findViewById<ImageView>(R.id.imageViewMovieType)

    private var movie: Movie? = null

    init {
        itemView.setOnClickListener {
            movie?.let { it1 -> onMovieClickListener.onClickListItem(it1) }
        }
    }

    fun bind(movieIn: Movie?) {
        movie = movieIn
        if (movie == null) {
            imageViewMovie.setImageDrawable(ResourcesCompat.getDrawable(this.itemView.resources, R.drawable.ic_broken_image, null))
        } else {
            val stringUrl = MFinService.Instance.IMAGE_BASE_URL_W500 + movie!!.backdropPath

            val stringUrlPosterPath = MFinService.Instance.IMAGE_BASE_URL_W500 + movie!!.posterPath

            val uri = stringUrl.toUri().buildUpon().scheme("https").build()
            val uriPoster = stringUrlPosterPath.toUri().buildUpon().scheme("https").build()
            imageViewMovie.load(uriPoster) {
                crossfade(true)
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
//            Glide.with(this.itemView.context)
//                .load(uri)
//                .apply(
//                    RequestOptions()
//                        .placeholder(R.drawable.loading_animation)
//                        .error(R.drawable.ic_broken_image))
//                .into(imageViewMovie)

            textViewMovieName.text = movie?.originalTitle

            chipGroupGenres.removeAllViews()
            movie?.genres?.forEach {
                val chip = Chip(this.itemView.context)
                chip.typeface = Typeface.create(
                    ResourcesCompat.getFont(
                        this.itemView.context, R.font.roboto_medium), Typeface.NORMAL)
                chip.text = it.name
                chipGroupGenres.addView(chip)
            }

            movie?.voteAverage?.let {
                chipVoteDetails.visibility = View.VISIBLE
                chipVoteDetails.text = this.itemView.resources.getString(R.string.vote_details,
                    movie!!.voteAverage, movie!!.voteCount)
                chipVoteDetails.chipBackgroundColor =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(this.itemView.context, android.R.color.holo_orange_light))
            }

            movie?.releaseDate?.let {
                val formatFrom = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formatTo = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                try {
                    val stringDate = formatFrom.parse(it)
                    stringDate?.let {
                        val stringDateOutput = formatTo.format(it)
                        textViewMovieReleaseDate.text = stringDateOutput
                    }
                } catch (e: Exception) {
                    Log.d("MovieViewHolder: " , "Release date: " + e.message)
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, onMovieClickListener: MovieAdapter.OnMovieClickListener): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.now_playing_list_item, parent, false)
            return MovieViewHolder(view, onMovieClickListener)
        }
    }
}
package com.kalann.moviefinder.movies

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kalann.moviefinder.R
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie

class MovieViewHolder(itemView: View, onMovieClickListener: MovieAdapter.OnMovieClickListener) : RecyclerView.ViewHolder(itemView) {
    val imageViewMovie = itemView.findViewById<ImageView>(R.id.imageViewMovie)
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
            val stringUrl = MFinService.Instance.IMAGE_BASE_URL + movie!!.backdropPath
            val uri = stringUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(this.itemView.context)
                .load(uri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image))
                .into(imageViewMovie)
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
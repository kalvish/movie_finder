package com.kalann.moviefinder.ui.now_playing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kalann.moviefinder.R
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie

class NwPlayingMoviesAdapter (val onMovieClickListener: OnMovieClickListener) : ListAdapter<Movie, NwPlayingMoviesAdapter.MovieViewHolder>(MoviesDiffUtil()) {
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewMovie = itemView.findViewById<ImageView>(R.id.imageViewMovie)
//        val imageViewMovieType = itemView.findViewById<ImageView>(R.id.imageViewMovieType)
    }

    class MoviesDiffUtil : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            //Referential equality
            //a === b evaluates to true if and only if a and b point to the same object
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater
            .from(parent.context)
            .inflate(R.layout.now_playing_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
//        if(getItem(position).type=="test"){
//            holder.imageViewMovieType.visibility = View.VISIBLE
//        }else{
//            holder.imageViewMovieType.visibility = View.GONE
//        }
        val stringUrl = MFinService.Instance.IMAGE_BASE_URL_W500 + getItem(position).backdropPath
        val uri = stringUrl.toUri().buildUpon().scheme("https").build()
        holder.imageViewMovie.load(stringUrl) {
            crossfade(true)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
//        Glide.with(holder.itemView.context)
//            .load(uri)
//            .apply(
//                RequestOptions()
//                .placeholder(R.drawable.loading_animation)
//                .error(R.drawable.ic_broken_image))
//            .into(holder.imageViewMovie)
        holder.itemView.setOnClickListener {
            onMovieClickListener.onClickListItem(getItem(position))
        }
    }

    interface OnMovieClickListener {
        fun onClickListItem(movie: Movie)
    }
}
package com.kalann.moviefinder.ui.now_playing

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.kalann.moviefinder.R
import com.kalann.moviefinder.api.MFinService
import com.kalann.moviefinder.api.moshi.Movie
import java.text.SimpleDateFormat
import java.util.*

class NwPlayingMoviesAdapter (val onMovieClickListener: OnMovieClickListener) : ListAdapter<Movie, NwPlayingMoviesAdapter.MovieViewHolder>(MoviesDiffUtil()) {
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewMovie = itemView.findViewById<ImageView>(R.id.imageViewMovie)
        val textViewMovieName = itemView.findViewById<AppCompatTextView>(R.id.textViewMovieName)
        val chipGroupGenres = itemView.findViewById<ChipGroup>(R.id.chipGroupGenres)
        val chipVoteDetails = itemView.findViewById<Chip>(R.id.chipVoteDetails)
        val textViewMovieReleaseDate = itemView.findViewById<AppCompatTextView>(R.id.textViewMovieReleaseDate)
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
        val movieToShow = getItem(position)
        val stringUrl = MFinService.Instance.IMAGE_BASE_URL_W500 + getItem(position).backdropPath
        val uri = stringUrl.toUri().buildUpon().scheme("https").build()
        holder.imageViewMovie.load(stringUrl) {
            crossfade(true)
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }

        holder.textViewMovieName.text = movieToShow?.originalTitle

        holder.chipGroupGenres.removeAllViews()
        movieToShow?.genres?.forEach {
            val chip = Chip(holder.itemView.context)
            chip.typeface = Typeface.create(
                ResourcesCompat.getFont(
                    holder.itemView.context, R.font.roboto_medium), Typeface.NORMAL)
            chip.text = it.name
            holder.chipGroupGenres.addView(chip)
        }

        movieToShow?.voteAverage?.let {
            holder.chipVoteDetails.visibility = View.VISIBLE
            holder.chipVoteDetails.text = holder.itemView.resources.getString(R.string.vote_details,
                movieToShow!!.voteAverage, movieToShow!!.voteCount)
            holder.chipVoteDetails.chipBackgroundColor =
                ColorStateList.valueOf(
                    ContextCompat.getColor(holder.itemView.context, android.R.color.holo_orange_light))
        }

        movieToShow?.releaseDate?.let {
            val formatFrom = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatTo = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                val stringDate = formatFrom.parse(it)
                stringDate?.let {
                    val stringDateOutput = formatTo.format(it)
                    holder.textViewMovieReleaseDate.text = stringDateOutput
                }
            } catch (e: Exception) {
                Log.d("MovieViewHolder: " , "Release date: " + e.message)
            }
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
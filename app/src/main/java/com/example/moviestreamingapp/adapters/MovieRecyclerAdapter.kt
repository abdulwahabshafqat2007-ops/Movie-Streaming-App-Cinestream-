package com.example.moviestreamingapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestreamingapp.databinding.ItemMovieRecyclerBinding
import com.example.moviestreamingapp.models.Movie

class MovieRecyclerAdapter(
    var movies: List<Movie> = emptyList(),
    val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieRecyclerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    class MovieViewHolder(
        private val binding: ItemMovieRecyclerBinding,
        val onMovieClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.apply {
                movieTitle.text = movie.title
                movieGenre.text = movie.genre
                movieRating.text = "⭐ ${movie.rating}"
                movieDuration.text = "• ${movie.duration}"
                movieYear.text = "${movie.year}"
                progressBar.progress = movie.watchProgress
                posterView.setBackgroundColor(Color.parseColor(movie.posterColor))
                root.setOnClickListener { onMovieClick(movie) }
            }
        }
    }
}
package com.example.moviestreamingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestreamingapp.databinding.ItemApiMovieBinding
import com.example.moviestreamingapp.models.ApiMovie

class ApiMovieAdapter(
    var movies: List<ApiMovie> = emptyList(),
    val onMovieClick: (ApiMovie) -> Unit
) : RecyclerView.Adapter<ApiMovieAdapter.ApiMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiMovieViewHolder {
        val binding = ItemApiMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ApiMovieViewHolder(binding, onMovieClick)
    }

    override fun onBindViewHolder(holder: ApiMovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<ApiMovie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    class ApiMovieViewHolder(
        private val binding: ItemApiMovieBinding,
        val onMovieClick: (ApiMovie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: ApiMovie) {
            binding.apply {
                apiMovieTitle.text = movie.name
                apiMovieGenre.text = movie.genres.joinToString(" • ").ifEmpty { "General" }
                apiMovieRating.text = "⭐ ${movie.rating?.average ?: "N/A"}"
                root.setOnClickListener { onMovieClick(movie) }
            }
        }
    }
}
package com.example.moviestreamingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestreamingapp.adapters.ApiMovieAdapter
import com.example.moviestreamingapp.databinding.FragmentApiMoviesBinding
import com.example.moviestreamingapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApiMoviesFragment : Fragment() {

    private lateinit var binding: FragmentApiMoviesBinding
    private lateinit var adapter: ApiMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApiMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ApiMovieAdapter(onMovieClick = { movie ->
            Toast.makeText(requireContext(), "Clicked: ${movie.name}", Toast.LENGTH_SHORT).show()
        })

        binding.apiRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ApiMoviesFragment.adapter
        }

        fetchMovies()
    }

    private fun fetchMovies() {
        binding.progressBar.visibility = View.VISIBLE
        binding.errorText.visibility = View.GONE

        // F1: Background thread using Coroutines
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val movies = RetrofitClient.apiService.getShows()
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    adapter.updateMovies(movies)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.errorText.visibility = View.VISIBLE
                    binding.errorText.text = "Failed to load: ${e.message}"
                }
            }
        }
    }
}
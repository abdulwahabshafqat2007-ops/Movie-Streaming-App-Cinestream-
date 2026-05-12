package com.example.moviestreamingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import com.example.moviestreamingapp.R
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestreamingapp.adapters.MovieRecyclerAdapter
import com.example.moviestreamingapp.databinding.FragmentSearchBinding
import com.example.moviestreamingapp.utils.MovieDataProvider

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var movieAdapter: MovieRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieRecyclerAdapter(
            movies = MovieDataProvider.getAllMovies()
        ) { movie ->
            val detailFragment = MovieDetailFragment.newInstance(movie)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredMovies = if (newText.isNullOrEmpty()) {
                    MovieDataProvider.getAllMovies()
                } else {
                    MovieDataProvider.searchMovies(newText)
                }
                movieAdapter.updateMovies(filteredMovies)
                return true
            }
        })
    }
}
package com.example.moviestreamingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import com.example.moviestreamingapp.R
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestreamingapp.adapters.MovieRecyclerAdapter
import com.example.moviestreamingapp.databinding.FragmentHomeBinding
import com.example.moviestreamingapp.utils.MovieDataProvider

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var movieAdapter: MovieRecyclerAdapter
    private var userName: String = ""
    private var planType: String = ""

    companion object {
        fun newInstance(userName: String, planType: String, userId: Int): HomeFragment {
            return HomeFragment().apply {
                arguments = Bundle().apply {
                    putString("USER_NAME", userName)
                    putString("PLAN_TYPE", planType)
                    putInt("USER_ID", userId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString("USER_NAME", "")
            planType = it.getString("PLAN_TYPE", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userGreeting.text = "Welcome, $userName!"

        movieAdapter = MovieRecyclerAdapter(
            movies = MovieDataProvider.getAllMovies()
        ) { movie ->
            val detailFragment = MovieDetailFragment.newInstance(movie)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.movieRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter
        }
    }
}
package com.example.moviestreamingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviestreamingapp.adapters.WatchlistAdapter
import com.example.moviestreamingapp.database.DatabaseHelper
import com.example.moviestreamingapp.database.FirestoreHelper
import com.example.moviestreamingapp.databinding.FragmentWatchlistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistFragment : Fragment() {

    private lateinit var binding: FragmentWatchlistBinding
    private lateinit var adapter: WatchlistAdapter
    private lateinit var dbHelper: DatabaseHelper
    private val firestoreHelper = FirestoreHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        adapter = WatchlistAdapter(
            onDeleteClick = { item ->
                lifecycleScope.launch(Dispatchers.IO) {
                    // Delete from Local SQLite
                    dbHelper.removeFromWatchlist(item.id)
                    // Delete from Firebase Firestore
                    firestoreHelper.removeWatchlistItem(item.movieId)
                    
                    val updated = dbHelper.getAllWatchlist()
                    withContext(Dispatchers.Main) {
                        adapter.updateItems(updated)
                        binding.emptyText.visibility = if (updated.isEmpty()) View.VISIBLE else View.GONE
                        Toast.makeText(requireContext(), "${item.title} removed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        binding.watchlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@WatchlistFragment.adapter
        }

        binding.btnSortByRating.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val sorted = dbHelper.getWatchlistSortedByRating()
                withContext(Dispatchers.Main) {
                    adapter.updateItems(sorted)
                }
            }
        }

        binding.watchlistSearchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val results = if (newText.isNullOrEmpty()) {
                            dbHelper.getAllWatchlist()
                        } else {
                            dbHelper.searchWatchlist(newText)
                        }
                        withContext(Dispatchers.Main) {
                            adapter.updateItems(results)
                        }
                    }
                    return true
                }
            })

        loadWatchlist()
    }

    override fun onResume() {
        super.onResume()
        loadWatchlist()
    }

    private fun loadWatchlist() {
        lifecycleScope.launch(Dispatchers.IO) {
            val items = dbHelper.getAllWatchlist()
            withContext(Dispatchers.Main) {
                adapter.updateItems(items)
                binding.emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}
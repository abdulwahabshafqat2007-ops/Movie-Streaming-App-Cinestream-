package com.example.moviestreamingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestreamingapp.databinding.ItemWatchlistBinding
import com.example.moviestreamingapp.models.WatchlistItem

class WatchlistAdapter(
    var items: List<WatchlistItem> = emptyList(),
    val onDeleteClick: (WatchlistItem) -> Unit
) : RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val binding = ItemWatchlistBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WatchlistViewHolder(binding, onDeleteClick)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<WatchlistItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    class WatchlistViewHolder(
        private val binding: ItemWatchlistBinding,
        val onDeleteClick: (WatchlistItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WatchlistItem) {
            binding.apply {
                watchlistTitle.text = item.title
                watchlistGenre.text = item.genre
                watchlistRating.text = "⭐ ${item.rating}"
                watchlistDate.text = "Added: ${item.addedDate}"
                btnDelete.setOnClickListener { onDeleteClick(item) }
            }
        }
    }
}
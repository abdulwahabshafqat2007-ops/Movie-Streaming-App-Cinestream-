package com.example.moviestreamingapp.models

data class WatchlistItem(
    val id: Int = 0,
    val movieId: Int,
    val title: String,
    val genre: String,
    val rating: Double,
    val addedDate: String
)
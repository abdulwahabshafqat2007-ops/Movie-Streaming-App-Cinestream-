package com.example.moviestreamingapp.models

import java.io.Serializable

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val rating: Double,
    val duration: String,
    val year: Int,
    val description: String,
    val posterColor: String,
    val watchProgress: Int = 0
) : Serializable
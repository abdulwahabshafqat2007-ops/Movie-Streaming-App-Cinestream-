package com.example.moviestreamingapp.models

data class ApiMovie(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val rating: ApiRating?,
    val summary: String?
)

data class ApiRating(
    val average: Double?
)
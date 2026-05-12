package com.example.moviestreamingapp.network

import com.example.moviestreamingapp.models.ApiMovie
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("shows")
    suspend fun getShows(
        @Query("page") page: Int = 0
    ): List<ApiMovie>
}
package com.example.moviesapp.retrofit

import com.example.moviesapp.data.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MoviesApi {
    @Headers("X-API-KEY: 9W9BCXY-9VV4P47-H64HH8Y-VC3M4H4", "accept: application/json")
    @GET("movie")
    suspend fun getMoviesList(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("notNullFields") notNullFields: String,
        ): Movies
}
package com.example.moviesapp.retrofit

import com.example.moviesapp.data.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MoviesApi {
    @Headers("X-API-KEY: HGDBFW5-MN44TNG-QEMC24G-SQWD7D7", "accept: application/json")
    @GET("movie")
    suspend fun getMoviesList(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("notNullFields") notNullFields: String,
        ): Movies
}
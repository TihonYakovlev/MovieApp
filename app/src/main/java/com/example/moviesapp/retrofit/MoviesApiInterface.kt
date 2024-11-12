package com.example.moviesapp.retrofit

import com.example.moviesapp.data.movie_details.MovieDetailsApi
import com.example.moviesapp.data.movies_list.Movies
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @Headers("X-API-KEY: $API_KEY", "accept: application/json")
    @GET("movie")
    suspend fun getMoviesList(
        @Query("page") page: String,
        @Query("limit") limit: String,
    ): Movies

    @Headers("X-API-KEY: $API_KEY", "accept: application/json")
    @GET("movie/search")
    suspend fun getMoviesBySearch(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("query") notNullFields: String,
    ): Movies

    @Headers("X-API-KEY: $API_KEY", "accept: application/json")
    @GET("movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int,
    ): MovieDetailsApi

    private companion object {
        private const val API_KEY = "HGDBFW5-MN44TNG-QEMC24G-SQWD7D7"
    }
}


package com.example.moviesapp.retrofit

import com.example.moviesapp.data.Countries
import com.example.moviesapp.data.movie_details.MovieDetailsApi
import com.example.moviesapp.data.movies_list.Movies
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @Headers("X-API-KEY: $API_KEY", "accept: application/json")
    @GET("v1.4/movie")
    suspend fun getMoviesList(
        @Query("page") page: String,
        @Query("limit") limit: String,
    ): Movies

    @Headers("X-API-KEY: $API_KEY", "accept: application/json")
    @GET("v1.4/movie/search")
    suspend fun getMoviesBySearch(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("query") notNullFields: String,
    ): Movies

    @Headers("X-API-KEY: $API_KEY", "accept: application/json")
    @GET("v1.4/movie/{id}")
    suspend fun getMovieById(
        @Path("id") id: Int,
    ): MovieDetailsApi

    @Headers("X-API-KEY: $API_KEY", "accept: application/json")
    @GET("v1/movie/possible-values-by-field")
    suspend fun getPossibleCountries(
        @Query("field") field: String,
    ): Countries

    private companion object {
        private const val API_KEY = "9W9BCXY-9VV4P47-H64HH8Y-VC3M4H4"
        //HGDBFW5-MN44TNG-QEMC24G-SQWD7D7
        //KA5C65N-22G4S79-JHDD0QM-T65RJ4A
    }
}


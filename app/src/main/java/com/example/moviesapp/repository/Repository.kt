package com.example.moviesapp.repository

import com.example.moviesapp.retrofit.RetrofitInstance
import com.example.moviesapp.viewmodels.MovieInfo

class Repository {
    suspend fun getMovies(page: Int, limit: Int): List<MovieInfo> {
        val movies =
            RetrofitInstance.api.getMoviesList(page.toString(), limit.toString(), "poster.url")
        return movies.docs.map {
            MovieInfo(
                id = it.id,
                name = it.name ?: (it.alternativeName ?: ""),
                rating = it.rating?.imdb ?: -1.0,
                releaseYear = it.releaseYear?.start ?: -1,
                poster = it.poster.url
            )
        }
    }
}
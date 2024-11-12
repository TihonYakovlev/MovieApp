package com.example.moviesapp.repository

import com.example.moviesapp.retrofit.RetrofitInstance
import com.example.moviesapp.viewmodels.MovieDetails
import com.example.moviesapp.viewmodels.MovieInfo

class Repository {
    suspend fun getMovies(page: Int, limit: Int): List<MovieInfo> {
        val movies =
            RetrofitInstance.api.getMoviesList(page.toString(), limit.toString())

        return movies.docs.map {
            MovieInfo(
                id = it.id,
                alternativeName = it.alternativeName ?: "",
                name = it.name ?: (it.alternativeName ?: ""),
                rating = it.rating?.imdb ?: -1.0,
                releaseYear = it.releaseYear?.start ?: -1,
                poster = it.poster?.url ?: ""
            )
        }
    }

    suspend fun getMoviesBySearch(page: Int, limit: Int, search: String): List<MovieInfo> {
        val movies =
            RetrofitInstance.api.getMoviesBySearch(page.toString(), limit.toString(), search)

        return movies.docs.map {
            MovieInfo(
                id = it.id,
                alternativeName = it.alternativeName ?: "",
                name = it.name ?: (it.alternativeName ?: ""),
                rating = it.rating?.imdb ?: -1.0,
                releaseYear = it.releaseYear?.start ?: -1,
                poster = it.poster?.url ?: ""
            )
        }
    }

    suspend fun getMovieById(id: Int): MovieDetails{
        val details = RetrofitInstance.api.getMovieById(id)
        return MovieDetails(
            id = details.id ?: -1,
            name = details.name ?: "",
            alternativeName = details.alternativeName ?: "",
            description = details.description ?: "",
            ageRating = details.ageRating ?: -1,
            countries = details.countries?.map { it.name } ?: emptyList(),
            genres = details.genres?.map { it.name } ?: emptyList(),
            logo = details.logo?.url ?: "",
            movieLength = details.movieLength ?: -1,
            persons = details.persons ?: emptyList(),
            rating = details.rating?.imdb ?: -1.0,
            type = details.type ?: "",
            votes = details.votes?.imdb ?: -1,
            year = details.year ?: -1
        )
    }

}


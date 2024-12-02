package com.example.moviesapp.repository

import com.example.moviesapp.data.movie_details.MovieDetailsApi
import com.example.moviesapp.data.movies_list.Movies
import com.example.moviesapp.retrofit.RetrofitInstance
import com.example.moviesapp.viewmodels.MovieDetails
import com.example.moviesapp.viewmodels.MovieInfo
import com.example.moviesapp.viewmodels.People
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository {
    suspend fun getMoviesBySearch(page: Int, limit: Int, search: String): List<MovieInfo> =
        withContext(Dispatchers.IO) {
            val movies =
                RetrofitInstance.api.getMoviesBySearch(page.toString(), limit.toString(), search)
            MoviesToListMapper().map(movies)
        }

    suspend fun getMovieById(id: Int): MovieDetails = withContext(Dispatchers.IO) {
        val details = RetrofitInstance.api.getMovieById(id)
        MovieDetailsApiToMovieDetails().map(details)
    }

    suspend fun getAllCountries(): List<String> = withContext(Dispatchers.IO) {
        val countries = RetrofitInstance.api.getPossibleCountries(field = "countries.name")
        countries.map { it.name }
    }

    suspend fun getMoviesWithFilters(
        page: Int,
        limit: Int,
        countries: List<String>,
        startYear: String,
        endYear: String,
        age: Set<Int>
    ): List<MovieInfo> = withContext(Dispatchers.IO) {

        val yearsString = "$startYear-$endYear"
        val ageDiapason = if (age.isNotEmpty()) "${age.min()}-${age.max()}" else "0-18"

        val movies =
            RetrofitInstance.api.getFilteredMoviesList(
                page = page.toString(),
                limit = limit.toString(),
                ageRating = ageDiapason,
                year = yearsString,
                countries = countries
            )

        MoviesToListMapper().map(movies)

    }
}

class MoviesToListMapper {
    fun map(model: Movies): List<MovieInfo> {
        return model.docs.map {
            MovieInfo(
                id = it.id,
                alternativeName = it.alternativeName ?: "",
                name = it.name ?: it.alternativeName ?: "",
                genre = it.genres?.first()?.name ?: "-",
                rating = if (it.rating?.imdb.toString() == "0.0") "" else it.rating?.imdb.toString(),
                year = if (it.year.toString() == "null") "-" else it.year.toString(),
                poster = it.poster?.url ?: ""
            )
        }
    }
}

class MovieDetailsApiToMovieDetails{
    fun map(details: MovieDetailsApi) : MovieDetails{
        return MovieDetails(
            id = details.id ?: -1,
            name = details.name ?: "-",
            alternativeName = details.alternativeName ?: "-",
            description = details.description ?: "Нет описания",
            ageRating = details.ageRating ?: -1,
            countries = details.countries?.map { it.name } ?: emptyList(),
            genres = details.genres?.map { it.name } ?: emptyList(),
            logo = details.poster?.previewUrl.orEmpty(),
            movieLength = details.movieLength ?: -1,
            persons = details.persons?.map {
                People(
                    name = it.name ?: "",
                    description = it.description ?: "",
                    photo = it.photo ?: "",
                    profession = it.profession ?: ""
                )
            } ?: emptyList(),
            rating = details.rating?.imdb ?: -1.0,
            type = details.type.orEmpty(),
            votes = details.votes?.imdb ?: -1,
            year = details.year ?: -1
        )
    }
}



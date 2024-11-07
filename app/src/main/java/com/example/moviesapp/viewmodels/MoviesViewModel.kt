package com.example.moviesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.Genre
import com.example.moviesapp.data.Rating
import com.example.moviesapp.data.ReleaseYear
import com.example.moviesapp.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieInfo(
    val id: Int,
    val name: String,
    val rating: Int,
    val releaseYear: Int,
)

class MoviesViewModel : ViewModel() {
    private val _movies = MutableStateFlow<List<MovieInfo>>(emptyList())
    val movies: StateFlow<List<MovieInfo>>
        get() = _movies.asStateFlow()

    fun fetchMoviesList(pageNumber: Int, limitOfMoviesOnPage: Int) {
        viewModelScope.launch {
            val response =
                RetrofitInstance.api.getMoviesList(
                    page = pageNumber.toString(),
                    limit = limitOfMoviesOnPage.toString()
                )

            _movies.update {
                response.docs.map {
                    MovieInfo(
                        id = it.id,
                        name = it.name ?: (it.alternativeName ?: ""),
                        rating = it.rating?.imdb ?: -1,
                        releaseYear = it.releaseYear?.start ?: -1
                    )
                }
            }

        }
    }
}
package com.example.moviesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieInfo(
    val id: Int,
    val alternativeName: String,
    val name: String,
    val rating: Double,
    val releaseYear: Int,
    val poster: String,
)


data class MoviesScreenState(
    val moviesList: List<MovieInfo> = emptyList(),
    val isNeedToLoadMoreMovies: Boolean = false,
    val isMoviesLoaded: Boolean = false,
)

class MoviesViewModel : ViewModel() {

    private val _movies = MutableStateFlow(MoviesScreenState())
    val movies: StateFlow<MoviesScreenState>
        get() = _movies.asStateFlow()

    private val repository = Repository()

    fun fetchMoviesList(pageNumber: Int, limitOfMoviesOnPage: Int) {
        viewModelScope.launch {
            _movies.update {
                MoviesScreenState(
                    moviesList = repository.getMovies(
                        page = pageNumber,
                        limit = limitOfMoviesOnPage
                    ),
                    isMoviesLoaded = true
                )
            }
        }
    }

}
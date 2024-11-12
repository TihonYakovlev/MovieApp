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
    var searchedMoviesList: List<MovieInfo> = emptyList(),
    var isLoading: Boolean = true,
)

class MoviesViewModel : ViewModel() {

    private val _movies = MutableStateFlow(MoviesScreenState())
    val movies: StateFlow<MoviesScreenState>
        get() = _movies.asStateFlow()

    private var page: Int = INITIAL_PAGE
    private var searchedPage: Int = INITIAL_SEARCHED_PAGE

    private val repository = Repository()

    fun loadNextPage() {
        viewModelScope.launch {
            val movies = repository.getMovies(
                page = page, limit = PAGE_SIZE
            )
            _movies.update { state ->
                state.copy(
                    moviesList = state.moviesList + movies, isLoading = false
                )
            }
            page++
        }
    }

    fun createNewSearch(){
        _movies.value.isLoading = true
        searchedPage = INITIAL_SEARCHED_PAGE
        _movies.value.searchedMoviesList = emptyList()
    }

    fun loadNextSearchedPage(query: String) {
        viewModelScope.launch {
            val searchedMovies =
                repository.getMoviesBySearch(page = searchedPage, limit = PAGE_SIZE, search = query)
            _movies.update { state ->
                state.copy(
                    searchedMoviesList = state.searchedMoviesList + searchedMovies,
                    isLoading = false
                )
            }
            searchedPage++
        }
    }

    private companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_PAGE = 1
        const val INITIAL_SEARCHED_PAGE = 1
    }
}
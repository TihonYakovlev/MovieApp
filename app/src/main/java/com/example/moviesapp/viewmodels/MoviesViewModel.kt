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
    val genre: String,
    val rating: String,
    val year: String,
    val poster: String,
)

data class MoviesScreenState(
    val moviesList: List<MovieInfo> = emptyList(),
    val isLoading: Boolean = true,
    val isNeedLoadFirstPage: Boolean = true,
)

class MoviesViewModel : ViewModel() {
    private val _movies = MutableStateFlow(MoviesScreenState())
    val movies: StateFlow<MoviesScreenState>
        get() = _movies.asStateFlow()

    private var page: Int = INITIAL_PAGE

    private val repository = Repository()

    fun loadNextPage() {
        viewModelScope.launch {
            try {
                val movies = repository.getMovies(
                    page = page, limit = PAGE_SIZE
                )
                _movies.update { state ->
                    state.copy(
                        moviesList = state.moviesList + movies, isLoading = false,
                        isNeedLoadFirstPage = false
                    )
                }
                page++
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    private companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_PAGE = 1
    }
}
package com.example.moviesapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.repository.Repository
import kotlinx.coroutines.delay
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

sealed class ViewState {
    data object EmptyScreen : ViewState()
    data class MoviesScreenState(
        val moviesList: List<MovieInfo>,
        val isNeedToLoadMoreMovies: Boolean,
    ) : ViewState()

    data object Loading : ViewState()
}

data class MoviesScreenState(
    val moviesList: List<MovieInfo> = emptyList(),
    val isLoading: Boolean = true,
    val isNeedToLoadMoreMovies: Boolean = false,
)

class MoviesViewModel : ViewModel() {

    private val _movies = MutableStateFlow(MoviesScreenState())
    val movies: StateFlow<MoviesScreenState>
        get() = _movies.asStateFlow()

    private var page: Int = INITIAL_PAGE

    private val repository = Repository()

    fun loadNextPage() {
        viewModelScope.launch {
            val movies = repository.getMovies(
                page = page,
                limit = PAGE_SIZE
            )
            _movies.update { state ->
                state.copy(
                    moviesList = state.moviesList + movies,
                    isLoading = false
                )
            }
            page++
        }
    }

    private companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_PAGE = 1
    }
}
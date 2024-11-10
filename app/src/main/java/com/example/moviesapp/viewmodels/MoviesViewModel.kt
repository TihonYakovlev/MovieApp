package com.example.moviesapp.viewmodels

import androidx.compose.runtime.mutableStateOf
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

sealed class ViewState {
    object EmptyScreen : ViewState()
    data class MoviesScreenState(
        val moviesList: List<MovieInfo>,
        val isNeedToLoadMoreMovies: Boolean,
    ) : ViewState()
    object Loading : ViewState()
}

data class MoviesScreenState(
    val moviesList: List<MovieInfo> = emptyList(),
    val isNeedToLoadMoreMovies: Boolean = false,
)

class MoviesViewModel : ViewModel() {

    private val _movies = MutableStateFlow(MoviesScreenState())
    val movies: StateFlow<MoviesScreenState>
        get() = _movies.asStateFlow()

//    private val _movies = MutableStateFlow<ViewState>(ViewState.Loading)
//    val movies: StateFlow<ViewState>
//        get() = _movies.asStateFlow()

    private val repository = Repository()

    private val lists = mutableListOf<MovieInfo>()
    private var pageNo = 1

    fun fetchMoviesList(pageNumber: Int, limitOfMoviesOnPage: Int) {
        viewModelScope.launch {
            _movies.update {
                MoviesScreenState(
                    moviesList = repository.getMovies(
                        page = pageNumber,
                        limit = limitOfMoviesOnPage
                    ),
                )
            }
        }
    }
}
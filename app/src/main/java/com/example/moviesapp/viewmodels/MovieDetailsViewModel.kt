package com.example.moviesapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.moviesapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DetailsScreenState(
    val moviesList: List<MovieDetails> = emptyList(),
    val isLoading: Boolean = true,
)

class MovieDetailsViewModel : ViewModel() {

    private val _movies = MutableStateFlow(DetailsScreenState())
    val movies: StateFlow<DetailsScreenState>
        get() = _movies.asStateFlow()

    private var page: Int = INITIAL_PAGE

    private val repository = Repository()


    private companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_PAGE = 1
    }
}
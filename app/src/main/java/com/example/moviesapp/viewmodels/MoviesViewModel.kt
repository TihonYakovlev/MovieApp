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
    val currentLazyListIndex: Int = 0,
)

class MoviesViewModel(private val filtersState: StateFlow<FiltersScreenState>) : ViewModel() {
    private val _movies = MutableStateFlow(MoviesScreenState())
    val movies: StateFlow<MoviesScreenState>
        get() = _movies.asStateFlow()

    init {
        viewModelScope.launch {
            filtersState.collect{
                resetMovies()
            }
        }
    }

    private var page: Int = INITIAL_PAGE

    private val repository = Repository()

    fun saveScreenState(newIndex: Int){
        _movies.update {
            state ->
            state.copy(
                currentLazyListIndex = newIndex
            )
        }
    }

    fun resetMovies() {
        println("ВЫЗВАЛСЯ РЕСЕТ")
        _movies.update { state ->
            state.copy(
                moviesList = emptyList(),
                isNeedLoadFirstPage = true,
                // isLoading = true
            )
        }
        page = INITIAL_PAGE
    }

    fun loadNextPageWithFilters() {
        println("ВЫЗВАЛСЯ loadNextPageWithFilters")

        viewModelScope.launch {
            try {
                val movies = repository.getMoviesWithFilters(
                    page = page,
                    limit = PAGE_SIZE,
                    countries = filtersState.value.selectedCountries.toList(),
                    startYear = filtersState.value.selectedStartYear,
                    endYear = filtersState.value.selectedEndYear,
                    age = filtersState.value.selectedAge
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
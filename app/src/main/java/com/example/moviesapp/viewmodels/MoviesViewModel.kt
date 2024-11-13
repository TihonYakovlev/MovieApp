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

    private val _allCountries = MutableStateFlow(emptyList<String>())
    val allCountries: StateFlow<List<String>>
        get() = _allCountries.asStateFlow()

    var selectedCountries = mutableListOf("")

    fun loadNextPage() {


        viewModelScope.launch {

            try {
                val movies = repository.getMovies(
                    page = page, limit = PAGE_SIZE
                )
                _movies.update { state ->
                    state.copy(
                        moviesList = state.moviesList + movies, isLoading = false
                    )
                }
                page++
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    fun createNewSearch() {
        _movies.value.isLoading = true
        searchedPage = INITIAL_SEARCHED_PAGE
        _movies.value.searchedMoviesList = emptyList()
    }

    fun loadNextSearchedPage(query: String) {



        viewModelScope.launch {

            try {
                val searchedMovies =
                    repository.getMoviesBySearch(page = searchedPage, limit = PAGE_SIZE, search = query)
                _movies.update { state ->
                    state.copy(
                        searchedMoviesList = state.searchedMoviesList + searchedMovies,
                        isLoading = false
                    )
                }
                searchedPage++
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    fun getAllCountries() {
        viewModelScope.launch {


            try {
                val gettingCountries = repository.getAllCountries()
                _allCountries.update {
                    gettingCountries
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    fun removeFromChosenCountries(country: String) {
        selectedCountries.add(country)
    }

    fun addToChosenCountries(country: String) {
        selectedCountries.remove(country)
    }

    private companion object {
        const val PAGE_SIZE = 20
        const val INITIAL_PAGE = 1
        const val INITIAL_SEARCHED_PAGE = 1
    }
}
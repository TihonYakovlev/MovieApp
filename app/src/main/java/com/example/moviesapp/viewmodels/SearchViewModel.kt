package com.example.moviesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchScreenState(
    val searchedMovies: List<MovieInfo> = emptyList(),
    val isLoading: Boolean = true,
    val isNeedLoadFirstPage: Boolean = true,
)

class SearchViewModel : ViewModel() {
    private val _searchedMovies = MutableStateFlow(SearchScreenState())
    val searchedMovies: StateFlow<SearchScreenState>
        get() = _searchedMovies.asStateFlow()

    private var searchedPage: Int = INITIAL_SEARCHED_PAGE
    var isLoadingNextPage = false
    private val repository = Repository()

    fun loadNextSearchedPage(query: String) {
        if(isLoadingNextPage) return

        isLoadingNextPage = true

        viewModelScope.launch {
            try {
                val searchedMovies =
                    repository.getMoviesBySearch(
                        page = searchedPage,
                        limit = PAGE_SIZE,
                        search = query
                    )
                _searchedMovies.update { state ->
                    state.copy(
                        searchedMovies = state.searchedMovies + searchedMovies,
                        isLoading = false,
                        isNeedLoadFirstPage = false
                    )
                }
                searchedPage++
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingNextPage = false
            }
        }
    }

    fun resetMovies() {
        _searchedMovies.update { state ->
            state.copy(
                searchedMovies = emptyList(),
                isNeedLoadFirstPage = true,
            )
        }
        searchedPage = INITIAL_SEARCHED_PAGE
    }

    private companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_SEARCHED_PAGE = 1
    }
}
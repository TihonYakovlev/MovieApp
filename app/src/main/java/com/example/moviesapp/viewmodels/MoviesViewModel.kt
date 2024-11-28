package com.example.moviesapp.viewmodels

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_datastore")

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
    val searchHistory: Queue<String> = LinkedList()
)

class MoviesViewModel(
    context: Context,
    private val filtersState: StateFlow<FiltersScreenState>
) : ViewModel() {
    private val _movies = MutableStateFlow(MoviesScreenState())
    val movies: StateFlow<MoviesScreenState>
        get() = _movies.asStateFlow()

    private val repository = Repository()
    private val dataStore = context.dataStore

    private companion object {
        const val PAGE_SIZE = 10
        const val INITIAL_PAGE = 1
    }

    private var page: Int = INITIAL_PAGE

    init {
        viewModelScope.launch {
            filtersState.collect {
                resetMovies()
            }
        }
        viewModelScope.launch {
            loadSearchHistory()
        }
    }

    fun saveScreenState(newIndex: Int) {
        _movies.update { state ->
            state.copy(
                currentLazyListIndex = newIndex
            )
        }
    }

    private fun resetMovies() {
        _movies.update { state ->
            state.copy(
                moviesList = emptyList(),
                isNeedLoadFirstPage = true,
            )
        }
        page = INITIAL_PAGE
    }

    fun loadNextPageWithFilters() {
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
                        moviesList = state.moviesList + movies,
                        isLoading = false,
                        isNeedLoadFirstPage = false
                    )
                }
                page++
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[DataStoreKeys.SEARCH_HISTORY] = emptySet()
            }
            loadSearchHistory()
        }
    }

    private suspend fun saveSearchQuery(query: String) {
        dataStore.edit { preferences ->
            val history = preferences[DataStoreKeys.SEARCH_HISTORY] ?: emptySet()
            val updatedHistory = (listOf(query) + history).take(20).toSet()
            preferences[DataStoreKeys.SEARCH_HISTORY] = updatedHistory
        }
    }

    private suspend fun getSearchHistory(): List<String> {
        return dataStore.data.map { preferences ->
            preferences[DataStoreKeys.SEARCH_HISTORY]?.toList() ?: emptyList()
        }.first()
    }

    fun addToSearchHistory(query: String) {
        viewModelScope.launch {
            saveSearchQuery(query)
            loadSearchHistory()
        }
    }

    fun loadSearchHistory() {
        viewModelScope.launch {
            val history = getSearchHistory()
            _movies.update { state ->
                state.copy(searchHistory = LinkedList(history))
            }
        }
    }
}

object DataStoreKeys {
    val SEARCH_HISTORY = stringSetPreferencesKey("search_history")
}
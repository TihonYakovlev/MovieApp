package com.example.moviesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.movie_details.Person
import com.example.moviesapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieDetails(
    val id: Int,
    val name: String,
    val alternativeName: String,
    val description: String,
    val ageRating: Int,
    val countries: List<String>,
    val genres: List<String>,
    val logo: String,
    val movieLength: Int,
    val persons: List<Person>,
    val rating: Double,
    val type: String,
    val votes: Int,
    val year: Int,
)

data class DetailsScreenState(
    val movieDetails: MovieDetails = MovieDetails(
        id = 0,
        name = "",
        alternativeName = "",
        description = "",
        ageRating = -1,
        countries = emptyList(),
        genres = emptyList(),
        logo = "",
        movieLength = -1,
        persons = emptyList(),
        rating = -1.0,
        type = "",
        votes = -1,
        year = -1,
    ),
    val isLoading: Boolean = true,
)

class MovieDetailsViewModel : ViewModel() {

    private val _details = MutableStateFlow(DetailsScreenState())
    val details: StateFlow<DetailsScreenState>
        get() = _details.asStateFlow()

    private val repository = Repository()

    fun getDetails(id: Int) {
        viewModelScope.launch {
            runCatching {
                val details = repository.getMovieById(id)
                println("Logo: $details")
                _details.update { state ->
                    state.copy(movieDetails = details, isLoading = false)
                }
            }
        }
    }
}
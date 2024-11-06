package com.example.moviesapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.Movies
import com.example.moviesapp.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException


class MoviesViewModel : ViewModel() {
//    private val _movies = mutableStateOf(Movies(emptyList(), 0, 0, 0, 0))
//    val movies: State<Movies> = _movies

    private val _movies = MutableStateFlow(Movies(emptyList(), 0, 0, 0, 0))
    val movies: StateFlow<Movies>
        get() = _movies.asStateFlow()

//    init {
//        fetchMoviesList("1", "10")
//    }


    fun fetchMoviesList(pageNumber: String, limitOfMoviesOnPage: String) {
        Log.d("view model", "fetch is called")
        viewModelScope.launch {
            Log.d("view model", "launch is called")



                val response =
                    RetrofitInstance.api.getMoviesList(
                        page = pageNumber,
                        limit = limitOfMoviesOnPage
                    )

            Log.d("view model", "instance is called")

            _movies.update { currentMovies ->
                Log.d("view model", "update is called")

                currentMovies.copy(
                    docs = response.docs,
                    limit = response.limit,
                    page = response.page,
                    pages = response.pages,
                    total = response.total
                )
            }
        }
    }
}
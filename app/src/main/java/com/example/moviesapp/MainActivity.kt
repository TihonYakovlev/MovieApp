package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.presentation.AppNavigation
import com.example.moviesapp.viewmodels.MovieDetailsViewModel
import com.example.moviesapp.viewmodels.MoviesViewModel
import com.example.moviesapp.viewmodels.SearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MoviesViewModel by viewModels()
            val detailsViewModel: MovieDetailsViewModel by viewModels()
            val searchedMoviesViewModel: SearchViewModel by viewModels()

            MoviesApp(
                viewModel = viewModel,
                detailsViewModel = detailsViewModel,
                searchedMoviesViewModel = searchedMoviesViewModel
            )
        }
    }
}

@Composable
fun MoviesApp(
    viewModel: MoviesViewModel,
    detailsViewModel: MovieDetailsViewModel,
    searchedMoviesViewModel: SearchViewModel
) {
    val navController = rememberNavController()
    AppNavigation(
        viewModel = viewModel,
        detailsViewModel = detailsViewModel,
        searchedMoviesViewModel = searchedMoviesViewModel,
        navController = navController
    )
}





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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MoviesViewModel by viewModels()
            val detailsViewModel: MovieDetailsViewModel by viewModels()
            MoviesApp(viewModel = viewModel, detailsViewModel = detailsViewModel)
        }
    }
}

@Composable
fun MoviesApp(viewModel: MoviesViewModel, detailsViewModel: MovieDetailsViewModel) {
    val navController = rememberNavController()
    AppNavigation(
        viewModel = viewModel,
        detailsViewModel = detailsViewModel,
        navController = navController
    )
}





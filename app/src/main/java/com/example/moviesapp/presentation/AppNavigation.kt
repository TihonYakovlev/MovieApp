package com.example.moviesapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.presentation.screens.MovieDetailScreen
import com.example.moviesapp.presentation.screens.MoviesListScreen
import com.example.moviesapp.viewmodels.MoviesViewModel

object Routes {
    val MoviesListScreen = "movies_list_screen"
    val MovieDetailsScreen = "movie_detail_screen"
}

@Composable
fun AppNavigation(viewModel: MoviesViewModel, modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MoviesListScreen, builder = {
        composable(Routes.MoviesListScreen) {
            MoviesListScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Routes.MovieDetailsScreen + "/{id}") {
            val id = it.arguments?.getString("id")
            MovieDetailScreen(
                modifier = modifier,
                viewModel = viewModel,
                id = id ?: "id not found"
            )
        }

    })

}
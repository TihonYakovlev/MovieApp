package com.example.moviesapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.presentation.screens.MovieDetailScreen
import com.example.moviesapp.presentation.screens.MoviesListScreen
import com.example.moviesapp.presentation.screens.SearchedMoviesScreen
import com.example.moviesapp.viewmodels.MovieDetailsViewModel
import com.example.moviesapp.viewmodels.MoviesViewModel

object Routes {
    val MoviesListScreen = "movies_list_screen"
    val MovieDetailsScreen = "movie_detail_screen"
    val SearchedMoviesScreen = "searched_movies_screen"
}

@Composable
fun AppNavigation(
    viewModel: MoviesViewModel,
    detailsViewModel: MovieDetailsViewModel,
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController = navController, startDestination = Routes.MoviesListScreen, builder = {
        composable(Routes.MoviesListScreen) {
            MoviesListScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Routes.SearchedMoviesScreen + "/{query}") {
            val query = it.arguments?.getString("query")

            SearchedMoviesScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                query = query ?: ""
            )
        }

        composable(Routes.MovieDetailsScreen + "/{id}") {
            val id = it.arguments?.getString("id")
            MovieDetailScreen(
                modifier = modifier,
                viewModel = detailsViewModel,
                id = id ?: "id not found"
            )
        }

    })

}
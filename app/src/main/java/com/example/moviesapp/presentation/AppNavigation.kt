package com.example.moviesapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moviesapp.presentation.screens.FiltersScreen
import com.example.moviesapp.presentation.screens.MovieDetailScreen
import com.example.moviesapp.presentation.screens.MoviesListScreen
import com.example.moviesapp.presentation.screens.SearchedMoviesScreen
import com.example.moviesapp.viewmodels.FiltersViewModel
import com.example.moviesapp.viewmodels.MovieDetailsViewModel
import com.example.moviesapp.viewmodels.MoviesViewModel
import com.example.moviesapp.viewmodels.SearchViewModel

object Routes {
    val MoviesListScreen = "movies_list_screen"
    val MovieDetailsScreen = "movie_detail_screen"
    val SearchedMoviesScreen = "searched_movies_screen"
    val FiltersScreen = "filters_screen"
}

@Composable
fun AppNavigation(
    viewModel: MoviesViewModel,
    detailsViewModel: MovieDetailsViewModel,
    searchedMoviesViewModel: SearchViewModel,
    filtersViewModel: FiltersViewModel,
    navController: NavHostController,
) {

    val modifier: Modifier = Modifier


    NavHost(navController = navController, startDestination = Routes.MoviesListScreen, builder = {
        composable(Routes.MoviesListScreen) {
            MoviesListScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Routes.SearchedMoviesScreen + "/{query}") {
            val query = it.arguments?.getString("query")

            SearchedMoviesScreen(
                viewModel = searchedMoviesViewModel,
                navController = navController,
                query = query ?: ""
            )
        }

        composable(Routes.MovieDetailsScreen + "/{id}") {
            val id = it.arguments?.getString("id")
            MovieDetailScreen(
                viewModel = detailsViewModel,
                id = id ?: "id not found",
                navController = navController
            )
        }

        composable(Routes.FiltersScreen) {
            FiltersScreen(filtersViewModel,modifier, navController)
        }

    })

}
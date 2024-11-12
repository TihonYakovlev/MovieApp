package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.moviesapp.viewmodels.MoviesViewModel

@Composable
fun SearchedMoviesScreen(modifier: Modifier, viewModel: MoviesViewModel, navController: NavController, query: String) {

    val screenState by viewModel.movies.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()

    val isScrolledToEnd = remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadNextSearchedPage(query)
    }

    LaunchedEffect(isScrolledToEnd.value) {
        if (isScrolledToEnd.value && !screenState.isLoading) {
            viewModel.loadNextSearchedPage(query)
        }
    }

    if (screenState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(4.dp),
            modifier = modifier,
            state = listState
        ) {
            itemsIndexed(screenState.searchedMoviesList) { _, movie ->
                MovieCard(movie, navController)
            }
            item {
                if (isScrolledToEnd.value) {
                    Box(
                        modifier = modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
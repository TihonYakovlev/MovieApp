package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.viewmodels.MoviesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchedMoviesScreen(viewModel: MoviesViewModel, navController: NavController, query: String) {
    MoviesAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Результаты поиска")
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Вернуться назад"
                            )
                        }
                    },

                    //   colors = TopAppBarColors()
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Bottom app bar",
                    )
                }
            },
            content = { innerPadding ->
                SearchedMoviesListScreenContent(
                    viewModel = viewModel,
                    modifier = Modifier
                        .padding(innerPadding)
                        .systemBarsPadding(),
                    navController = navController,
                    query = query
                )
            }
        )
    }
}

@Composable
fun SearchedMoviesListScreenContent(
    viewModel: MoviesViewModel,
    modifier: Modifier,
    navController: NavController,
    query: String
) {
    val screenState by viewModel.movies.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()

    val isScrolledToEnd = remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.let { item ->
                item.index == listState.layoutInfo.totalItemsCount - 1
            } ?: true
        }
    }

    LaunchedEffect(isScrolledToEnd.value) {
        if (isScrolledToEnd.value) {
            viewModel.loadNextSearchedPage(query)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSearch()
        }
    }


        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(4.dp),
            modifier = modifier,
            state = listState
        ) {

            if (screenState.isLoading) {
                item {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                itemsIndexed(screenState.searchedMoviesList) { _, movie ->
                    MovieCard(movie, navController)
                }
            }

            if (isScrolledToEnd.value && screenState.isLoading) {
                item {
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

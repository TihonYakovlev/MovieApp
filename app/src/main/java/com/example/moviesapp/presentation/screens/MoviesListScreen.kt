package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.moviesapp.R
import com.example.moviesapp.presentation.Routes
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.viewmodels.MovieInfo
import com.example.moviesapp.viewmodels.MoviesViewModel
import com.example.moviesapp.viewmodels.SearchViewModel

@Composable
fun MoviesListScreen(
    viewModel: MoviesViewModel,
    searchedViewModel: SearchViewModel,
    navController: NavController
) {
    MoviesAppTheme {
        Scaffold(
            topBar = {
                TopBarContent(
                    viewModel = viewModel,
                    searchedViewModel = searchedViewModel,
                    navController = navController
                )
            },
            content = { innerPadding ->
                MoviesListScreenContent(
                    viewModel = viewModel,
                    modifier = Modifier
                        .padding(innerPadding)
                        .systemBarsPadding(),
                    navController = navController
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent(
    viewModel: MoviesViewModel,
    searchedViewModel: SearchViewModel,
    navController: NavController
) {

    val screenState = viewModel.movies.collectAsState()

    var searchValue by remember {
        mutableStateOf("")
    }

    val isSearchBarActive = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        SearchBar(
            query = searchValue,
            onQueryChange = { newQuery ->
                searchValue = newQuery
            },
            onSearch = {
                isSearchBarActive.value = false
                if (searchValue.isNotEmpty()) {
                    searchedViewModel.resetMovies()
                    viewModel.addToSearchHistory(searchValue)
                    navController.navigate(route = Routes.SearchedMoviesScreen + "/${searchValue}")
                }
            },
            active = isSearchBarActive.value,
            onActiveChange = {
                isSearchBarActive.value = it
                if (it) {
                    viewModel.loadSearchHistory()
                }
            },
            placeholder = { Text(text = stringResource(R.string.search_bar_hint)) },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    val history = screenState.value.searchHistory.toList()
                    if (history.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.search_history),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(8.dp)
                            )
                            TextButton(
                                onClick = {
                                    viewModel.clearSearchHistory()
                                }
                            ) {
                                Text(text = stringResource(R.string.clear_history))
                            }
                        }

                        LazyColumn {
                            items(history) { query ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                        .clickable {
                                            searchValue = query
                                            isSearchBarActive.value = false
                                            searchedViewModel.resetMovies()
                                            viewModel.addToSearchHistory(query)
                                            navController.navigate(route = Routes.SearchedMoviesScreen + "/${query}")
                                        },
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = query,
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.no_search_history),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        )
        IconButton(
            onClick = {
                navController.navigate(Routes.FiltersScreen)
            }
        ) {
            Icon(
                Icons.AutoMirrored.TwoTone.List,
                contentDescription = stringResource(R.string.filters)
            )
        }
    }
}

@Composable
fun MoviesListScreenContent(
    viewModel: MoviesViewModel,
    modifier: Modifier, navController: NavController
) {
    val screenState by viewModel.movies.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()
    val isScrolledToEnd = remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewModel.saveScreenState(listState.firstVisibleItemIndex)
        }
    }
    LaunchedEffect(Unit) {
        if (screenState.isNeedLoadFirstPage) {
            viewModel.loadNextPageWithFilters()
        }
    }
    LaunchedEffect(isScrolledToEnd.value && !viewModel.isLoadingNextPage) {
        if (isScrolledToEnd.value) {
            println("RIJRIJRIJRIJRIJRIJRIJRIJRIJRIJRIJRIJRIJRIJRIJRIJRIJRIJR")
            viewModel.loadNextPageWithFilters()
        }
    }
    if (screenState.isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp), contentPadding = PaddingValues(4.dp),
            modifier = modifier, state = listState
        ) {
            itemsIndexed(screenState.moviesList) { _, movie ->
                MovieCard(movie, navController)
            }
            if (isScrolledToEnd.value && (listState.layoutInfo.totalItemsCount > 5)) {
                item {
                    Box(
                        modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: MovieInfo,
    navController: NavController
) {
    val moviePoster = rememberAsyncImagePainter(
        if (movie.poster != "") {
            movie.poster
        } else {
            R.drawable.no_foto
        }
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(150.dp)
            .padding(4.dp)
            .clickable {
                navController.navigate(route = Routes.MovieDetailsScreen + "/${movie.id}")
            }, colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            //verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.25f),
                contentAlignment = Alignment.CenterStart,
            ) {
                Image(
                    painter = moviePoster,
                    contentDescription = stringResource(R.string.movie_poster),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = movie.name, fontStyle = FontStyle.Normal,
                    fontSize = 20.sp, letterSpacing = TextUnit.Unspecified,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(text = movie.alternativeName, fontStyle = FontStyle.Italic)
                Text(text = "${movie.genre}, ${movie.year}")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp), contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = movie.rating, fontWeight = FontWeight.Bold)
            }
        }
    }
}
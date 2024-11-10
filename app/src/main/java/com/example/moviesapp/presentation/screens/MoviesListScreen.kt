package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberImagePainter
import com.example.moviesapp.viewmodels.MovieInfo
import com.example.moviesapp.viewmodels.MoviesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MoviesListScreen(modifier: Modifier, viewModel: MoviesViewModel) {

    val screenState by viewModel.movies.collectAsStateWithLifecycle()
    val listState = rememberLazyGridState()

    val isScrolledToEnd = remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadNextPage()
    }

    LaunchedEffect(isScrolledToEnd.value) {
        if(isScrolledToEnd.value){
            viewModel.loadNextPage()
        }
    }

    if (screenState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
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
            itemsIndexed(screenState.moviesList) { _, movie ->
                MovieCard(movie)
            }
            item {
                if (isScrolledToEnd.value) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: MovieInfo) {
    val moviePoster = rememberImagePainter(data = movie.poster)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(150.dp)
            .padding(4.dp),
        // .background(color = Color.White),
        colors = CardColors(
            contentColor = Color.Black,
            disabledContentColor = Color.Black,
            containerColor = Color.White,
            disabledContainerColor = Color.Red
        ), elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {

        Row {

            Image(
                painter = moviePoster,
                contentDescription = "Poster",
                modifier = Modifier.width(100.dp)
            )

            Column(
                modifier = Modifier
                    .background(color = Color.White)
                    .padding(10.dp)
            ) {
                Text(
                    text = movie.name,
                    fontStyle = FontStyle.Normal,
                    fontSize = 20.sp,
                    letterSpacing = TextUnit.Unspecified,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
                Text(text = movie.alternativeName, fontStyle = FontStyle.Italic)
            }

            Text(text = movie.rating.toString())

        }
    }
}

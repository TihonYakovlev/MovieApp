package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviesapp.viewmodels.MovieInfo

@Composable
fun MoviesListScreen(modifier: Modifier, movies: List<MovieInfo>) {
    LazyVerticalGrid(GridCells.Adaptive(300.dp), contentPadding = PaddingValues(4.dp), modifier = modifier.statusBarsPadding()) {
        itemsIndexed(movies) { _, movie ->
            MovieCard(modifier, movie)
        }
    }
}

@Composable
fun MovieCard(modifier: Modifier, movie: MovieInfo) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(150.dp)
            .padding(4.dp)
            //.background(color = Color.Red)
    ) {

Column {
    Text(movie.name)
}

    }
}

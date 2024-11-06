package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviesapp.data.Doc

@Composable
fun MoviesListScreen(movies: List<Doc?>) {


    LazyVerticalGrid(GridCells.Adaptive(150.dp), contentPadding = PaddingValues(4.dp)) {

        itemsIndexed(movies) { _, movie ->
            MovieCard(movie)
        }

    }
}

@Composable
fun MovieCard(movie: Doc?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(300.dp)
            .padding(4.dp)
    ) {

        movie?.let {
            Text(movie.name)
        }

    }
}
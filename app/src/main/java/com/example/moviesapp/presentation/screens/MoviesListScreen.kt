package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.moviesapp.viewmodels.MovieInfo

@Composable
fun MoviesListScreen(modifier: Modifier, movies: List<MovieInfo>) {
    LazyVerticalGrid(
        GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(4.dp),
        modifier = modifier
    ) {
        itemsIndexed(movies) { _, movie ->
            MovieCard(movie)
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
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {

        Row {

            Image(painter = moviePoster, contentDescription = "Poster")

            Column(modifier = Modifier
                .background(color = Color.White)
                .padding(5.dp)) {
                Text(text = movie.name, fontStyle = FontStyle.Normal)
            }
        }



    }
}

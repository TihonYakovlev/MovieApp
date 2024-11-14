package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.moviesapp.R
import com.example.moviesapp.viewmodels.MovieDetailsViewModel

@Composable
fun MovieDetailScreen(viewModel: MovieDetailsViewModel, id: String) {
    val screenState by viewModel.details.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val details = screenState.movieDetails

    val movieLogo = rememberAsyncImagePainter(
        if (details.logo != "") {
            println("LOGO ${details.logo}")
            details.logo
        } else {
            R.drawable.no_foto
        }

    )

    LaunchedEffect(Unit) {
        viewModel.getDetails(id = id.toInt())
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()) {
        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.Red)) {

            item {
                Text("${details.logo}")
            }

            item {
                Image(
                    painter = movieLogo,
                    contentDescription = "Poster",
                    modifier = Modifier.fillMaxSize(),
                )
            }

            item {
                Text("Details screen of movie with description = ${screenState.movieDetails.description}")
            }

        }
    }
}
package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.moviesapp.R
import com.example.moviesapp.viewmodels.MovieDetailsViewModel

@Composable
fun MovieDetailScreen(viewModel: MovieDetailsViewModel, id: String) {
    val screenState by viewModel.details.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getDetails(id = id.toInt())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = screenState.movieDetails.logo,
            contentDescription = "",
            success = { state ->
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .requiredHeight(100.dp),
                    painter = state.painter,
                    contentDescription = "Poster"
                )
            },
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .requiredHeight(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            error = {
                Image(
                    painter = painterResource(R.drawable.no_foto),
                    contentDescription = null
                )
            }
        )

        Text("Details screen of movie with description = ${screenState.movieDetails.description}")
    }
}
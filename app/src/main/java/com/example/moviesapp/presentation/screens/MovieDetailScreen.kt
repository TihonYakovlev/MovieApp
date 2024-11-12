package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviesapp.viewmodels.MovieDetailsViewModel
import kotlinx.coroutines.launch

@Composable
fun MovieDetailScreen(modifier: Modifier, viewModel: MovieDetailsViewModel, id: String) {
    val screenState by viewModel.details.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.getDetails(id = id.toInt())
        }
    }
    Box(modifier = modifier.fillMaxSize()) {
        Text("Details screen of movie with description = ${screenState.movieDetails.description}")
    }
}
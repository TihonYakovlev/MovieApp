package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviesapp.viewmodels.MovieDetailsViewModel

@Composable
fun MovieDetailScreen(modifier: Modifier, viewModel: MovieDetailsViewModel, id: String){
    val screenState by viewModel.movies.collectAsStateWithLifecycle()
    Box(modifier = modifier.fillMaxSize()){
        Text("Details screen of movie with description = ${screenState.moviesList}")
    }
}
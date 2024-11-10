package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moviesapp.viewmodels.MoviesViewModel

@Composable
fun MovieDetailScreen(modifier: Modifier, viewModel: MoviesViewModel, id: String){
    Box(modifier = modifier.fillMaxSize()){
        Text("Details screen of movie with id = $id")
    }
}
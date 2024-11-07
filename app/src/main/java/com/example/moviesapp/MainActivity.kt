package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.moviesapp.presentation.screens.MoviesListScreen
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.viewmodels.MoviesViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MoviesViewModel by viewModels()
            MoviesAppTheme {



                val coroutineScope = rememberCoroutineScope()
                val docs = viewModel.movies.collectAsState()

                LaunchedEffect(key1 = Unit) {
                    coroutineScope.launch {
                        viewModel.fetchMoviesList(pageNumber = 1, limitOfMoviesOnPage = 10)
                    }
                }
                MoviesListScreen(docs.value)
            }
        }
    }
}


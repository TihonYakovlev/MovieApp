package com.example.moviesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.moviesapp.presentation.screens.MoviesListScreen
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.viewmodels.MoviesViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MoviesViewModel by viewModels()
            MoviesAppTheme {

                //  Log.d("main activity", "viewModel.movies.value.docs = ${viewModel.movies.value.docs}")
                val docs = viewModel.movies.collectAsState().value.docs
                Log.d("main activity", "viewModel.movies.value.docs = $docs")

                Column {
                    Text("OIJOKN")
                    Button(onClick = {
                        viewModel.fetchMoviesList(
                            "1",
                            "10"
                        )
                        Log.d ("button", "button is clicked")
                    }) { Text("OIJOKN")}
                }
                MoviesListScreen(docs)
            }
        }
    }
}


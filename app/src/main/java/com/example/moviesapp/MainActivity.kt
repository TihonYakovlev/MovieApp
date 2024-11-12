package com.example.moviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.rememberNavController
import com.example.moviesapp.presentation.AppNavigation
import com.example.moviesapp.presentation.Routes
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.viewmodels.MovieDetailsViewModel
import com.example.moviesapp.viewmodels.MoviesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: MoviesViewModel by viewModels()
            val detailsViewModel: MovieDetailsViewModel by viewModels()


            MoviesApp(viewModel = viewModel, detailsViewModel = detailsViewModel)

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesApp(viewModel: MoviesViewModel, detailsViewModel: MovieDetailsViewModel) {

    var searchValue by remember {
        mutableStateOf("")
    }

    var isSearchClicked by remember {
        mutableStateOf(false)
    }
    val navController = rememberNavController()


    MoviesAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (!isSearchClicked)
                            Text("Movies")
                    },
                    actions = {
                        if (isSearchClicked) {
                            OutlinedTextField(value = searchValue, onValueChange = { newValue ->
                                searchValue = newValue
                            }, modifier = Modifier.fillMaxWidth(0.8f))
                            IconButton(onClick = {
                                isSearchClicked = false
                                searchValue = ""
                            }) { Icon(Icons.Filled.Close, contentDescription = "Поиск") }
                        }

                        IconButton(onClick = {
                            isSearchClicked = true
                            if (searchValue.isNotEmpty())
                                navController.navigate(route = Routes.SearchedMoviesScreen + "/${searchValue}")
                        }) { Icon(Icons.Filled.Search, contentDescription = "Поиск") }
                    }
                    //   colors = TopAppBarColors()
                )
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Bottom app bar",
                    )
                }
            },
            content = { innerPadding ->
                AppNavigation(
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(paddingValues = innerPadding),
                    viewModel = viewModel,
                    detailsViewModel = detailsViewModel,
                    navController = navController
                )
            }
        )
    }
}


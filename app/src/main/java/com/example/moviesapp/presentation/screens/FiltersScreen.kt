package com.example.moviesapp.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviesapp.presentation.Routes
import com.example.moviesapp.viewmodels.MoviesViewModel
import kotlinx.coroutines.launch

@Composable
fun FiltersScreen(viewModel: MoviesViewModel, modifier: Modifier, navController: NavController) {

    val coroutineScope = rememberCoroutineScope()

    var startYear by remember {
        mutableStateOf("")
    }

    var endYear by remember {
        mutableStateOf("")
    }

    val isCountryChosen = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
         //   viewModel.getAllCountries()
        }
    }

    Box(modifier = modifier.fillMaxSize())

    Column {
        Text("По возрастному ограничению")
        Text("По году")
        Row {
            OutlinedTextField(
                value = startYear,
                onValueChange = { newStartYear -> startYear = newStartYear })
            OutlinedTextField(
                value = endYear,
                onValueChange = { newEndYear -> startYear = newEndYear })
        }

        Card(modifier = Modifier.fillMaxWidth()) { Text("По стране") }

        LazyColumn {
            itemsIndexed(viewModel.allCountries.value) { _, country ->
                Text(text = country, modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable { if (isCountryChosen.value){
                        isCountryChosen.value = false
                        viewModel.removeFromChosenCountries(country)
                    } else {
                        isCountryChosen.value = true
                        viewModel.addToChosenCountries(country)
                    }
                    }
                )
            }
        }

        Button(onClick = {navController.navigate(Routes.MoviesListScreen)}) { Text("Применить") }

    }


}
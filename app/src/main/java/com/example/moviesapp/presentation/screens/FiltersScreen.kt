package com.example.moviesapp.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviesapp.presentation.Routes
import com.example.moviesapp.viewmodels.FiltersViewModel
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun FiltersScreen(viewModel: FiltersViewModel, modifier: Modifier, navController: NavController) {

    val coroutineScope = rememberCoroutineScope()

    var startYear by remember { mutableStateOf("") }
    var endYear by remember { mutableStateOf("") }
    var selectedAge by remember { mutableStateOf<String?>(null) }
    var selectedCountries by remember { mutableStateOf(mutableSetOf<String>()) }
    var isCountriesExpanded by remember { mutableStateOf(false) }

    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    val screenState = viewModel.filters.collectAsState()
    val countriesListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        coroutineScope.launch { viewModel.getAllCountries() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("По возрастному ограничению")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("0+", "6+", "12+", "18+").forEach { age ->
                Card(
                    modifier = Modifier
                        .clickable {
                            selectedAge = if (selectedAge == age) null else age
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedAge == age) androidx.compose.ui.graphics.Color.Cyan else androidx.compose.ui.graphics.Color.LightGray
                    )
                ) {
                    Text(age, modifier = Modifier.padding(8.dp))
                }
            }
        }

        Text("По году", modifier = Modifier.padding(top = 16.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = startYear,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.toIntOrNull()
                            ?.let { it in 1868..currentYear } == true) {
                        startYear = newValue
                    }
                },
                label = { Text("От") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )
            OutlinedTextField(
                value = endYear,
                onValueChange = { newValue ->

                },
                label = { Text("До") },
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clickable { isCountriesExpanded = !isCountriesExpanded }
        ) {
            Text(
                text = if (isCountriesExpanded) "Скрыть список стран" else "Выбрать страну",
                modifier = Modifier.padding(16.dp)
            )
        }

        if (isCountriesExpanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                state = countriesListState
            ) {
                itemsIndexed(screenState.value.allCountries) { _, country ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .clickable {
                                if (selectedCountries.contains(country)) {
                                    selectedCountries.remove(country)
                                } else {
                                    selectedCountries.add(country)
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedCountries.contains(country)) androidx.compose.ui.graphics.Color.Cyan else androidx.compose.ui.graphics.Color.White
                        )
                    ) {
                        Text(
                            text = country,
                            modifier = Modifier.padding(16.dp),
                            color = androidx.compose.ui.graphics.Color.Black
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                selectedAge = null
                startYear = ""
                endYear = ""
                selectedCountries.clear()
            }) { Text("Сбросить") }
            Button(onClick = { navController.navigate(Routes.MoviesListScreen) }) { Text("Применить") }
        }
    }
}

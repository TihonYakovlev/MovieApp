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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moviesapp.R
import com.example.moviesapp.presentation.Routes
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.example.moviesapp.viewmodels.FiltersViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun FiltersScreen(viewModel: FiltersViewModel, modifier: Modifier, navController: NavController) {
    MoviesAppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.filters),
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.navigate_back)
                            )
                        }
                    },
                )
            },
            content = { innerPadding ->
                FiltersContent(
                    viewModel = viewModel,
                    modifier = modifier.padding(innerPadding),
                    navController = navController
                )
            }
        )
    }
}


@Composable
fun FiltersContent(viewModel: FiltersViewModel, modifier: Modifier, navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    var isCountriesExpanded by remember { mutableStateOf(false) }

    val screenState = viewModel.filters.collectAsState()
    val allCountries = viewModel.allCountries.collectAsState()

    val selectedAge = screenState.value.selectedAge
    val startYear = screenState.value.selectedStartYear
    val endYear = screenState.value.selectedEndYear
    val selectedCountries = screenState.value.selectedCountries

    val countriesListState = rememberLazyListState()
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) {
        coroutineScope.launch { viewModel.getAllCountries() }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = stringResource(R.string.age),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(0, 6, 12, 18).forEach { age ->
                Card(
                    modifier = Modifier
                        .clickable { viewModel.updateSelectedAge(age) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedAge.contains(age))
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = "$age${stringResource(R.string.plus)}",
                        modifier = Modifier.padding(8.dp),
                        color = if (selectedAge.contains(age))
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.year),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            OutlinedTextField(
                value = startYear,
                onValueChange = { viewModel.updateSelectedStartYear(it) },
                label = { Text(text = stringResource(R.string.from)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = endYear,
                onValueChange = { viewModel.updateSelectedEndYear(it) },
                label = { Text(text = stringResource(R.string.to)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clickable { isCountriesExpanded = !isCountriesExpanded },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = if (isCountriesExpanded)
                    stringResource(R.string.hide_list_of_countries)
                else
                    stringResource(R.string.show_list_of_countries),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        if (isCountriesExpanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                state = countriesListState
            ) {
                itemsIndexed(allCountries.value) { _, country ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .clickable { viewModel.updateSelectedCountries(country) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedCountries.contains(country))
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = country,
                            modifier = Modifier.padding(16.dp),
                            color = if (selectedCountries.contains(country))
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
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
            Button(onClick = { viewModel.clearFilters() }) {
                Text(
                    text = stringResource(R.string.clear),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Button(onClick = { navController.navigate(Routes.MoviesListScreen) }) {
                Text(
                    text = stringResource(R.string.apply),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

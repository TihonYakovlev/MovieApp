package com.example.moviesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

data class FiltersScreenState(
    val selectedAge: Set<Int> = setOf(0, 18),
    val selectedCountries: Set<String> = emptySet(),
    val selectedStartYear: String = "1874",
    val selectedEndYear: String = Calendar.getInstance().get(Calendar.YEAR).toString(),
)

class FiltersViewModel : ViewModel() {
    private val _filters = MutableStateFlow(FiltersScreenState())
    val filters = _filters.asStateFlow()

    private val _allCountries = MutableStateFlow<List<String>>(emptyList())
    val allCountries = _allCountries.asStateFlow()

    private val repository = Repository()

    fun updateSelectedAge(newValue: Int) {

        _filters.update { state ->
            val updatedAge = if (state.selectedAge.contains(newValue)) {
                state.selectedAge - newValue
            } else {
                state.selectedAge + newValue
            }
            state.copy(
                selectedAge = updatedAge
            )
        }
    }

    fun updateSelectedStartYear(newValue: String) {
        _filters.update { state ->
            state.copy(
                selectedStartYear = newValue
            )
        }
    }

    fun updateSelectedEndYear(newValue: String) {
        _filters.update { state ->
            state.copy(
                selectedEndYear = newValue
            )
        }
    }

    fun updateSelectedCountries(newCountry: String) {
        _filters.update { state ->
            val updatedCountries = if (state.selectedCountries.contains(newCountry)) {
                state.selectedCountries - newCountry
            } else {
                state.selectedCountries + newCountry
            }
            state.copy(selectedCountries = updatedCountries)
        }
    }

    fun updateFilters(
        selectedAge: Set<Int>,
        selectedCountries: Set<String>,
        selectedStartYear: String,
        selectedEndYear: String
    ) {
        _filters.update { state ->
            state.copy(
                selectedAge = selectedAge,
                selectedCountries = selectedCountries,
                selectedStartYear = selectedStartYear,
                selectedEndYear = selectedEndYear,
            )
        }
    }

    fun getAllCountries() {
        viewModelScope.launch {
            try {
                val gettingCountries = repository.getAllCountries()
                _allCountries.update {
                    gettingCountries
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
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

            if (newValue == -1) {
                state.copy(
                    selectedAge = emptySet()
                )
            } else {
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
    }

    fun updateSelectedStartYear(newValue: String) {

        val handeledStartYear =
            if (newValue.isNotEmpty())  if (newValue.length == 4 && newValue.toInt() < 1874) "1874" else newValue else ""

        _filters.update { state ->
            state.copy(
                selectedStartYear = handeledStartYear
            )
        }
    }

    fun updateSelectedEndYear(newValue: String) {

        val handeledEndYear =
            if (newValue.isNotEmpty()) if (newValue.toInt() > 2050) "2050" else newValue else ""

        _filters.update { state ->
            state.copy(
                selectedEndYear = handeledEndYear
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
            if (newCountry.isNotEmpty()) {
                state.copy(selectedCountries = updatedCountries)
            } else {
                state.copy(selectedCountries = emptySet())
            }
        }
    }

    fun clearFilters() {
        updateSelectedAge(-1)
        updateSelectedStartYear("1874")
        updateSelectedEndYear("2050")
        updateSelectedCountries("")
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
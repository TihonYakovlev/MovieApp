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
    val allCountries: List<String> = emptyList(),
    val selectedAge: String = "",
    val selectedCountries: Set<String> = emptySet(),
    val selectedStartYear: Int = 1868,
    val selectedEndYear: Int = Calendar.getInstance().get(Calendar.YEAR),
)

class FiltersViewModel : ViewModel() {
    val _filters = MutableStateFlow(FiltersScreenState())
    val filters = _filters.asStateFlow()

    val repository = Repository()


    

    fun getAllCountries() {
        viewModelScope.launch {
            try {
                val gettingCountries = repository.getAllCountries()
                _filters.update {
                        state ->
                    state.copy(
                        allCountries = gettingCountries
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
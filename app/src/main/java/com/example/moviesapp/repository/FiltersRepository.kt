package com.example.moviesapp.repository

import com.example.moviesapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class FiltersRepository(
    val allCountries: List<String> = emptyList(),
    val selectedAge: String = "",
    val selectedCountries: Set<String> = emptySet(),
    val selectedStartYear: Int = 1868,
    val selectedEndYear: Int = Calendar.getInstance().get(Calendar.YEAR),
) {



    fun getFilters(): FiltersRepository {
        return this
    }

}
package com.example.moviesapp.data.movie_details

data class MovieDetailsApi(
    val id: Int?,
    val name: String?,
    val alternativeName: String?,
    val description: String?,
    val ageRating: Int?,
    val countries: List<Country>?,
    val genres: List<Genre>?,
    val logo: Logo?,
    val movieLength: Int?,
    val persons: List<Person>?,
    val rating: Rating?,
    val type: String?,
    val votes: Votes?,
    val year: Int?
)
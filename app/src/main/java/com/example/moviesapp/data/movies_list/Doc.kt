package com.example.moviesapp.data.movies_list

data class Doc(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val genres: List<Genre>?,
    val year: Int?,
    val rating: Rating?,
    val poster: Poster?,
)
package com.example.moviesapp.data

data class Doc(
    val id: Int,
    val name: String?,
    val alternativeName: String?,
    val genre: Genre?,
    val rating: Rating?,
    val releaseYear: ReleaseYear?,
    val poster: Poster,
)
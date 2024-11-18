package com.example.moviesapp.data.movies_list

data class Movies(
    val docs: List<Doc>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)
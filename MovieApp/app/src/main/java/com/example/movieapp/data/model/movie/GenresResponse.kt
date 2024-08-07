package com.example.movieapp.data.model.movie

import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres")
    val genres: List<GenreResponse>?
)

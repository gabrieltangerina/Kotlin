package com.example.movieapp.domain.repository.movie

import com.example.movieapp.data.model.CreditResponse
import com.example.movieapp.data.model.MovieResponse

interface MovieDetailsRepository {

    suspend fun getMovieDetails(
        apiKey: String?,
        language: String?,
        movieId: Int?
    ): MovieResponse

    suspend fun getCredits(
        apiKey: String?,
        language: String?,
        movieId: Int?
    ): CreditResponse

}
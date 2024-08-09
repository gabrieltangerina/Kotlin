package com.example.movieapp.domain.api.repository.movie

import com.example.movieapp.data.model.movie.CreditResponse
import com.example.movieapp.data.model.movie.MovieResponse
import com.example.movieapp.data.model.movie.MovieReviewResponse

interface MovieDetailsRepository {

    suspend fun getMovieDetails(
        movieId: Int?
    ): MovieResponse

    suspend fun getCredits(
        movieId: Int?
    ): CreditResponse

    suspend fun getSimilarMovies(
        movieId: Int?
    ): List<MovieResponse>

    suspend fun getMovieReviews(
        movieId: Int?
    ): List<MovieReviewResponse>

}
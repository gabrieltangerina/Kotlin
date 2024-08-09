package com.example.movieapp.data.repository.movie

import com.example.movieapp.data.api.ServiceAPI
import com.example.movieapp.data.model.movie.CreditResponse
import com.example.movieapp.data.model.movie.MovieResponse
import com.example.movieapp.data.model.movie.MovieReviewResponse
import com.example.movieapp.domain.api.repository.movie.MovieDetailsRepository
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val serviceAPI: ServiceAPI
) : MovieDetailsRepository {

    override suspend fun getMovieDetails(
        movieId: Int?
    ): MovieResponse {
        return serviceAPI.getMovieDetails(
            movieId = movieId
        )
    }

    override suspend fun getCredits(
        movieId: Int?
    ): CreditResponse {
        return serviceAPI.getCredits(
            movieId = movieId
        )
    }

    override suspend fun getSimilarMovies(
        movieId: Int?
    ): List<MovieResponse> {
        return serviceAPI.getSimilarMovies(
            movieId = movieId
        ).results ?: emptyList()
    }

    override suspend fun getMovieReviews(
        movieId: Int?
    ): List<MovieReviewResponse> {
        return serviceAPI.getMovieReviews(
            movieId = movieId
        ).results ?: emptyList()
    }

}
package com.example.movieapp.domain.api.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.MovieReview
import com.example.movieapp.domain.api.repository.movie.MovieDetailsRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {

        suspend operator fun invoke(apiKey: String, language: String?, movieId: Int?): List<MovieReview> {
        return movieDetailsRepository.getMovieReviews(
            apiKey = apiKey,
            language = language,
            movieId = movieId
        ).map { it.toDomain() }
    }

}
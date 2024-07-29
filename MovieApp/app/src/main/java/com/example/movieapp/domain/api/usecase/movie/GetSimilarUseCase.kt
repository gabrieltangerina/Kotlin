package com.example.movieapp.domain.api.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Credit
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.api.repository.movie.MovieDetailsRepository
import javax.inject.Inject

class GetSimilarUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {

        suspend operator fun invoke(apiKey: String, language: String?, movieId: Int?): List<Movie> {
        return movieDetailsRepository.getSimilarMovies(
            apiKey = apiKey,
            language = language,
            movieId = movieId
        ).filter { it.backdropPath != null }.map { it.toDomain() }
    }

}
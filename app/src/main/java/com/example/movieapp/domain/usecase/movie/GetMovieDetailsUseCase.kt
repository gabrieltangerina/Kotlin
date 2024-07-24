package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.movie.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(apiKey: String, language: String?, movieId: Int?): Movie {
        return movieRepository.getMovieDetails(
            apiKey = apiKey,
            language = language,
            movieId = movieId
        ).toDomain()
    }

}
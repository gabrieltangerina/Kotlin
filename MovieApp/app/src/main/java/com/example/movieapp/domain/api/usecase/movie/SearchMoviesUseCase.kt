package com.example.movieapp.domain.api.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.api.repository.movie.MovieRepository
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(apiKey: String, language: String?, query: String?): List<Movie> {
        return movieRepository.searchMovies(
            apiKey = apiKey,
            language = language,
            query = query
        ).filter { it.backdropPath != null }.map { it.toDomain() }
    }

}
package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.movie.MovieRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(apiKey: String, language: String?, genreId: Int?): List<Movie> {
        return movieRepository.getMoviesByGenre(
            apiKey = apiKey,
            language = language,
            genreId = genreId
        ).map { it.toDomain() }
    }

}
package com.example.movieapp.domain.api.usecase.movie

import com.example.movieapp.data.model.MovieResponse
import com.example.movieapp.domain.api.repository.movie.MovieRepository
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        genreId: Int?
    ): List<MovieResponse>  {
        return movieRepository.getMoviesByGenre(genreId).results ?: emptyList()
    }

}
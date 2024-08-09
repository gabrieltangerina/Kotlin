package com.example.movieapp.domain.api.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.movie.Genre
import com.example.movieapp.domain.api.repository.movie.MovieRepository
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(): List<Genre> {
        return movieRepository.getGenres().genres?.map { it.toDomain() } ?: emptyList()
    }

}
package com.example.movieapp.domain.local.usecase

import com.example.movieapp.data.mapper.toEntity
import com.example.movieapp.domain.local.repository.MovieLocalRepository
import com.example.movieapp.domain.model.movie.Movie
import javax.inject.Inject


class InsertMovieUseCase @Inject constructor(
    private val repository: MovieLocalRepository
) {

    suspend operator fun invoke(movie: Movie) {
        return repository.insertMovie(movie.toEntity())
    }
}
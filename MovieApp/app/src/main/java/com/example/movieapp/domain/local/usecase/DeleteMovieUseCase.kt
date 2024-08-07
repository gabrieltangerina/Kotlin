package com.example.movieapp.domain.local.usecase

import com.example.movieapp.domain.local.repository.MovieLocalRepository
import javax.inject.Inject


class DeleteMovieUseCase @Inject constructor(
    private val repository: MovieLocalRepository
) {

    suspend operator fun invoke(movieId: Int?) {
        return repository.deleteMovie(movieId)
    }
}
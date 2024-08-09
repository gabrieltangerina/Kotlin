package com.example.movieapp.domain.local.usecase

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.local.repository.MovieLocalRepository
import com.example.movieapp.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetMoviesUseCase @Inject constructor(
    private val repository: MovieLocalRepository
) {

    operator fun invoke(): Flow<List<Movie>> {
        return repository.getMovies().map {movieList ->
            movieList.map { it.toDomain() }
        }
    }

}
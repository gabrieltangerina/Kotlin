package com.example.movieapp.domain.api.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.api.repository.movie.MovieDetailsRepository
import com.example.movieapp.domain.api.repository.movie.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {

    suspend operator fun invoke( movieId: Int?): Movie {
        return movieDetailsRepository.getMovieDetails(
            movieId = movieId
        ).toDomain()
    }

}
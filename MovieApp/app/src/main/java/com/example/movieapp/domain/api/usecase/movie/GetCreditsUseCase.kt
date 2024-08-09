package com.example.movieapp.domain.api.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.movie.Credit
import com.example.movieapp.domain.api.repository.movie.MovieDetailsRepository
import javax.inject.Inject

class GetCreditsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {

        suspend operator fun invoke(movieId: Int?): Credit {
        return movieDetailsRepository.getCredits(
            movieId = movieId
        ).toDomain()
    }

}
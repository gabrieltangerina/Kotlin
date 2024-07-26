package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Credit
import com.example.movieapp.domain.repository.movie.MovieDetailsRepository
import javax.inject.Inject

class GetCreditsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {

        suspend operator fun invoke(apiKey: String, language: String?, movieId: Int?): Credit {
        return movieDetailsRepository.getCredits(
            apiKey = apiKey,
            language = language,
            movieId = movieId
        ).toDomain()
    }

}
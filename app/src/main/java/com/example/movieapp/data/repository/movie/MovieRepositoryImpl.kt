package com.example.movieapp.data.repository.movie

import com.example.movieapp.data.api.ServiceAPI
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse
import com.example.movieapp.domain.repository.movie.MovieRepository
import com.example.movieapp.util.Constants
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val serviceAPI: ServiceAPI
): MovieRepository {

    override suspend fun getGenres(apiKey: String, language: String?): GenresResponse {
        return serviceAPI.getGenres(
            apiKey = apiKey,
            language = Constants.Movie.language
        )
    }

    override suspend fun getMoviesByGenre(
        apiKey: String,
        language: String?,
        genreId: Int?
    ): List<MovieResponse> {
        return serviceAPI.getMoviesByGenre(
            apiKey = apiKey,
            language = Constants.Movie.language,
            genreId = genreId
        ).results ?: emptyList()
    }

}
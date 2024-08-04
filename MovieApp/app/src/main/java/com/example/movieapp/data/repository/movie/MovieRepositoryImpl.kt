package com.example.movieapp.data.repository.movie

import androidx.paging.PagingSource
import com.example.movieapp.data.api.ServiceAPI
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse
import com.example.movieapp.data.paging.MovieByGenrePagingSource
import com.example.movieapp.data.paging.SearchMoviePagingSource
import com.example.movieapp.domain.api.repository.movie.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val serviceAPI: ServiceAPI
) : MovieRepository {

    override suspend fun getGenres(apiKey: String?, language: String?): GenresResponse {
        return serviceAPI.getGenres(
            apiKey = apiKey,
            language = language
        )
    }

    override fun getMoviesByGenre(
        apiKey: String?,
        language: String?,
        genreId: Int?
    ): PagingSource<Int, MovieResponse> {
        return MovieByGenrePagingSource(serviceAPI, genreId)
    }

    override fun searchMovies(
        apiKey: String?,
        language: String?,
        query: String?,
    ):  PagingSource<Int, MovieResponse> {
       return SearchMoviePagingSource(serviceAPI, query)
    }

}
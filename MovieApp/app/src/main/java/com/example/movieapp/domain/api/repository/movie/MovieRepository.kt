package com.example.movieapp.domain.api.repository.movie

import androidx.paging.PagingSource
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse

interface MovieRepository {

    suspend fun getGenres(apiKey: String?, language: String?): GenresResponse

    fun getMoviesByGenre(
        apiKey: String?,
        language: String?,
        genreId: Int?
    ): PagingSource<Int, MovieResponse>

    fun searchMovies(
        apiKey: String?,
        language: String?,
        query: String?
    ): PagingSource<Int, MovieResponse>

}
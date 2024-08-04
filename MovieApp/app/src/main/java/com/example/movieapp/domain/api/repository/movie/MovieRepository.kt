package com.example.movieapp.domain.api.repository.movie

import androidx.paging.PagingSource
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse

interface MovieRepository {

    suspend fun getGenres(): GenresResponse

    fun getMoviesByGenre(
        genreId: Int?
    ): PagingSource<Int, MovieResponse>

    fun searchMovies(
        query: String?
    ): PagingSource<Int, MovieResponse>

}
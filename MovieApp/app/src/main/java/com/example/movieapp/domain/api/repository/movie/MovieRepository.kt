package com.example.movieapp.domain.api.repository.movie

import androidx.paging.PagingSource
import com.example.movieapp.data.model.BasePaginationRemote
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse

interface MovieRepository {

    suspend fun getGenres(): GenresResponse

    fun getMoviesByGenrePagination(
        genreId: Int?
    ): PagingSource<Int, MovieResponse>

    suspend fun getMoviesByGenre(
        genreId: Int?
    ): BasePaginationRemote<List<MovieResponse>>

    fun searchMovies(
        query: String?
    ): PagingSource<Int, MovieResponse>

}
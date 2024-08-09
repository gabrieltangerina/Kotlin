package com.example.movieapp.data.repository.movie

import androidx.paging.PagingSource
import com.example.movieapp.data.api.ServiceAPI
import com.example.movieapp.data.model.movie.BasePaginationRemote
import com.example.movieapp.data.model.movie.GenresResponse
import com.example.movieapp.data.model.movie.MovieResponse
import com.example.movieapp.data.paging.MovieByGenrePagingSource
import com.example.movieapp.data.paging.SearchMoviePagingSource
import com.example.movieapp.domain.api.repository.movie.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val serviceAPI: ServiceAPI
) : MovieRepository {

    override suspend fun getGenres(): GenresResponse {
        return serviceAPI.getGenres()
    }

    override fun getMoviesByGenrePagination(
        genreId: Int?
    ): PagingSource<Int, MovieResponse> {
        return MovieByGenrePagingSource(serviceAPI, genreId)
    }

    override suspend fun getMoviesByGenre(
        genreId: Int?
    ): BasePaginationRemote<List<MovieResponse>> {
        return serviceAPI.getMoviesByGenre(genreId)
    }

    override fun searchMovies(
        query: String?
    ): PagingSource<Int, MovieResponse> {
        return SearchMoviePagingSource(serviceAPI, query)
    }

}
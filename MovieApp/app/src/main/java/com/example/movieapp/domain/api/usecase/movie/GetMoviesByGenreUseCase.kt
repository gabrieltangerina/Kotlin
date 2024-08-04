package com.example.movieapp.domain.api.usecase.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.api.repository.movie.MovieRepository
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.util.Constants.Paging.DEFAULT_PAGE_INDEX
import com.example.movieapp.util.Constants.Paging.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    operator fun invoke(
        genreId: Int?
    ): Flow<PagingData<Movie>>  = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = DEFAULT_PAGE_INDEX
        ),
        pagingSourceFactory = {
            movieRepository.getMoviesByGenre(
                genreId
            )
        }
    ).flow.map { pagingData ->
        pagingData.map { movieResponse ->
            movieResponse.toDomain()
        }
    }

}
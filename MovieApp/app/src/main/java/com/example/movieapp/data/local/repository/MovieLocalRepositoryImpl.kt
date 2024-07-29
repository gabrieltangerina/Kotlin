package com.example.movieapp.data.local.repository

import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.domain.local.repository.MovieLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieLocalRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieLocalRepository {

    override fun getMovies(): Flow<List<MovieEntity>> {
        return movieDao.getMovies()
    }

    override suspend fun insertMovie(movieEntity: MovieEntity) {
        movieDao.insertMovie(movieEntity)
    }

    override suspend fun deleteMovie(movieId: Int?) {
        movieDao.deleteMovie(movieId)
    }
}
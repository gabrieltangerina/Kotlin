package com.example.movieapp.domain.local.repository

import com.example.movieapp.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MovieLocalRepository {

    fun getMovies(): Flow<List<MovieEntity>>

    suspend fun insertMovie(movieEntity: MovieEntity)

    suspend fun deleteMovie(movieId: Int?)

}
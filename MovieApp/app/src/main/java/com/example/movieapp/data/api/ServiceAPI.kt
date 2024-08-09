package com.example.movieapp.data.api

import com.example.movieapp.data.model.movie.BasePaginationRemote
import com.example.movieapp.data.model.movie.CreditResponse
import com.example.movieapp.data.model.movie.GenresResponse
import com.example.movieapp.data.model.movie.MovieResponse
import com.example.movieapp.data.model.movie.MovieReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceAPI {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenrePagination(
        @Query("with_genres") genreId: Int?,
        @Query("page") page: Int?
    ): BasePaginationRemote<List<MovieResponse>>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int?
    ): BasePaginationRemote<List<MovieResponse>>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String?,
        @Query("page") page: Int?
    ): BasePaginationRemote<List<MovieResponse>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int?,
    ): MovieResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int?,
    ): CreditResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int?,
    ): BasePaginationRemote<List<MovieResponse>>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int?,
    ): BasePaginationRemote<List<MovieReviewResponse>>

}
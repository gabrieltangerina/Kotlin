package com.example.movieapp.data.api

import com.example.movieapp.data.model.BasePaginationRemote
import com.example.movieapp.data.model.CreditResponse
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse
import com.example.movieapp.data.model.MovieReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceAPI {

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String?,
        @Query("language") language: String?
    ): GenresResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String?,
        @Query("language") language: String?,
        @Query("with_genres") genreId: Int?,
        @Query("page") page: Int?
    ): BasePaginationRemote<List<MovieResponse>>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String?,
        @Query("language") language: String?,
        @Query("query") query: String?,
        @Query("page") page: Int?
    ): BasePaginationRemote<List<MovieResponse>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String?,
        @Query("language") language: String?
    ): MovieResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String?,
        @Query("language") language: String?
    ): CreditResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String?,
        @Query("language") language: String?
    ): BasePaginationRemote<List<MovieResponse>>

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String?,
        @Query("language") language: String?
    ): BasePaginationRemote<List<MovieReviewResponse>>

}
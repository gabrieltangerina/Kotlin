package com.example.movieapp.di

import com.example.movieapp.data.local.repository.MovieLocalRepositoryImpl
import com.example.movieapp.data.repository.auth.FirebaseAuthenticationImpl
import com.example.movieapp.data.repository.movie.MovieDetailsRepositoryImpl
import com.example.movieapp.data.repository.movie.MovieRepositoryImpl
import com.example.movieapp.data.repository.user.UserRepositoryImpl
import com.example.movieapp.domain.api.repository.auth.FirebaseAuthentication
import com.example.movieapp.domain.api.repository.movie.MovieDetailsRepository
import com.example.movieapp.domain.api.repository.movie.MovieRepository
import com.example.movieapp.domain.api.repository.user.UserRepository
import com.example.movieapp.domain.local.repository.MovieLocalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindsFirebaseAuthenticationImpl(
        firebaseAuthenticationImpl: FirebaseAuthenticationImpl
    ): FirebaseAuthentication

    @Binds
    abstract fun bindsMovieRepositoryImpl(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    abstract fun bindsMovieDetailsRepositoryImpl(
        movieDetailsRepositoryImpl: MovieDetailsRepositoryImpl
    ): MovieDetailsRepository

    @Binds
    abstract fun bindsMovieLocalRepositoryImpl(
        movieLocalRepositoryImpl: MovieLocalRepositoryImpl
    ): MovieLocalRepository

    @Binds
    abstract fun bindsUserRepositoryImpl(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

}
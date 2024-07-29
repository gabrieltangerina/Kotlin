package com.example.movieapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.db.AppDatabase
import com.example.movieapp.util.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    // Responsável por fazer a criação do banco de dados
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): RoomDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        Database.MOVIE_DATABASE
    ).build()

    @Provides
    fun providesMovieDao(database: AppDatabase): MovieDao = database.movieDao()

}
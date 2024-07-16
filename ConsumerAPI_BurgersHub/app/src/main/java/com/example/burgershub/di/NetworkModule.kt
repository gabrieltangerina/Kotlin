package com.example.burgershub.di

import android.content.Context
import com.example.burgershub.network.ServiceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesServiceProvider(@ApplicationContext context: Context) = ServiceProvider(context)

}
package com.example.movieapp.di

import com.example.movieapp.data.api.ServiceAPI
import com.example.movieapp.network.ServiceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun providesServiceProvider() = ServiceProvider()

    @Provides
    fun providerServiceApi(
        serviceProvider: ServiceProvider
    ): ServiceAPI {
        return serviceProvider.createService(ServiceAPI::class.java)
    }

}
package com.example.burgershub.di

import com.example.burgershub.data.repository.BurgerRepositoryImpl
import com.example.burgershub.domain.repository.BurgerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindsBurgerRepositoryImpl(
        burgerRepositoryImpl: BurgerRepositoryImpl
    ): BurgerRepository

}
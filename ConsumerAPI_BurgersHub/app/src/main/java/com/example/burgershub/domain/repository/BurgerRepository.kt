package com.example.burgershub.domain.repository

import com.example.burgershub.data.model.BurgerResponse

interface BurgerRepository {

    suspend fun getBurgers(): List<BurgerResponse>

    suspend fun getBurgersById(id: Int): BurgerResponse

    suspend fun getBurgersByName(name: String): List<BurgerResponse>

}
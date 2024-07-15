package com.example.burgershub.data.repository

import com.example.burgershub.data.api.ServiceAPI
import com.example.burgershub.data.model.BurgerResponse
import com.example.burgershub.domain.repository.BurgerRepository
import javax.inject.Inject

class BurgerRepositoryImpl @Inject constructor(
    private val serviceAPI: ServiceAPI
) : BurgerRepository {

    override suspend fun getBurgers(): List<BurgerResponse> {
        return serviceAPI.getBurgers()
    }

    override suspend fun getBurgersById(burgerId: Int): BurgerResponse {
        return serviceAPI.getBurgersById(burgerId)
    }

    override suspend fun getBurgersByName(name: String): List<BurgerResponse> {
        return serviceAPI.getBurgersByName(name)
    }

}
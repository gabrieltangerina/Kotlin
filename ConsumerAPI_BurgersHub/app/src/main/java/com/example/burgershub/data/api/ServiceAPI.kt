package com.example.burgershub.data.api

import com.example.burgershub.data.model.BurgerResponse
import io.github.brunogabriel.mockpinterceptor.MOCK
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceAPI {

    @MOCK(asset = "burgers_response.json", runDelay = true)
    @GET("burgers")
    suspend fun getBurgers(): List<BurgerResponse>

    @MOCK(asset = "burger_response.json", runDelay = true)
    @GET("burgers/{burger_id}")
    suspend fun getBurgersById(
        @Path("burger_id") burgerId: Int
    ): BurgerResponse


    @MOCK(asset = "burger_name_response.json", runDelay = true)
    @GET("find-burger/?search={}")
    suspend fun getBurgersByName(
        @Query("search") name: String
    ): List<BurgerResponse>

}
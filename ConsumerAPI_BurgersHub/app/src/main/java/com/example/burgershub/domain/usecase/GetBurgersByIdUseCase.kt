package com.example.burgershub.domain.usecase

import com.example.burgershub.data.mapper.toDomain
import com.example.burgershub.domain.model.Burger
import com.example.burgershub.domain.repository.BurgerRepository
import javax.inject.Inject

class GetBurgersByIdUseCase @Inject constructor(
    private val burgerRepository: BurgerRepository
) {

    /*
    * Com o "operator ... invoke" voce pode chamar usando apenas GetBurgersUseCase() e nao
    * GetBurgersUseCase.getBurgers()
    */
    suspend operator fun invoke(burgerId: Int): Burger {
        return burgerRepository.getBurgersById(burgerId).toDomain()
    }

}
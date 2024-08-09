package com.example.movieapp.domain.api.usecase.user

import com.example.movieapp.domain.api.repository.user.UserRepository
import com.example.movieapp.domain.model.user.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): User {
        return userRepository.getUser()
    }

}
package com.example.movieapp.domain.api.repository.user

import com.example.movieapp.domain.model.user.User

interface UserRepository {

    suspend fun update(user: User)

    suspend fun getUser(): User

}
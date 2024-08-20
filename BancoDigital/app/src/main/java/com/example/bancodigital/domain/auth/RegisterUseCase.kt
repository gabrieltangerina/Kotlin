package com.example.bancodigital.domain.auth

import com.example.bancodigital.data.model.User
import com.example.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val firebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {

    suspend operator fun invoke(
        name: String,
        email: String,
        phone: String,
        password: String
    ): User {
        return firebaseDataSourceImpl.register(name, email, phone, password)
    }

}
package com.example.bancodigital.domain.auth

import com.example.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl

class RegisterUseCase(
    private val firebaseDataSourceImpl: AuthFirebaseDataSourceImpl
) {

    suspend operator fun invoke(name: String, email: String, phone: String, password: String) {
        return firebaseDataSourceImpl.register(name, email, phone, password)
    }

}
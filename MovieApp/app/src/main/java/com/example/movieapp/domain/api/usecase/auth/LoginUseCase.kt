package com.example.movieapp.domain.api.usecase.auth

import com.example.movieapp.domain.api.repository.auth.FirebaseAuthentication
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication
) {

    suspend operator fun invoke(email: String, password: String){
        firebaseAuthentication.login(email, password)
    }

}
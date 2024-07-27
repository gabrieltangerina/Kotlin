package com.example.movieapp.domain.usecase.auth

import com.example.movieapp.domain.repository.auth.FirebaseAuthentication
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication
) {

    suspend operator fun invoke(email: String){
        firebaseAuthentication.forgotPassword(email)
    }

}
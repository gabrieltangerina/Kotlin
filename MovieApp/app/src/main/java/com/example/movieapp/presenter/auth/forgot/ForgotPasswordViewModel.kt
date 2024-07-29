package com.example.movieapp.presenter.auth.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.api.usecase.auth.ForgotPasswordUseCase
import com.example.movieapp.domain.api.usecase.auth.LoginUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    fun forgotPassword(email: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            forgotPasswordUseCase.invoke(email)

            emit(StateView.Success(Unit))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        }
    }

}
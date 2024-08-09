package com.example.movieapp.presenter.main.bottombar.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.api.usecase.user.GetUserUseCase
import com.example.movieapp.domain.api.usecase.user.UserUpdateUseCase
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUpdateUseCase: UserUpdateUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    fun update(user: User) = liveData(Dispatchers.IO){
        try {

            emit(StateView.Loading())

            userUpdateUseCase.invoke(user)

            emit(StateView.Success(Unit))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(message = e.message))
        }
    }

    fun getUser() = liveData(Dispatchers.IO){
        try {

            emit(StateView.Loading())

            val user = getUserUseCase()

            emit(StateView.Success(user))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(message = e.message))
        }
    }

}
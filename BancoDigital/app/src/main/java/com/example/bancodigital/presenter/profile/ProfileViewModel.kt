package com.example.bancodigital.presenter.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.data.model.User
import com.example.bancodigital.domain.profile.GetProfileUseCase
import com.example.bancodigital.domain.profile.SaveImageProfileUseCase
import com.example.bancodigital.domain.profile.SaveProfileUseCase
import com.example.bancodigital.domain.profile.UpdateProfileUseCase
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val saveProfileUseCase: SaveProfileUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val saveImageProfileUseCase: SaveImageProfileUseCase
) : ViewModel() {

    fun saveProfile(user: User) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveProfileUseCase.invoke(user)

            emit(StateView.Success(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun getProfileUseCase() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val profile = getProfileUseCase.invoke(FirebaseHelper.getUserId())

            emit(StateView.Success(profile))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun updateProfile(user: User) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            updateProfileUseCase.invoke(user)

            emit(StateView.Success(user))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveImageProfile(imageProfile: String) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val urlImage = saveImageProfileUseCase.invoke(imageProfile)

            emit(StateView.Success(urlImage))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}
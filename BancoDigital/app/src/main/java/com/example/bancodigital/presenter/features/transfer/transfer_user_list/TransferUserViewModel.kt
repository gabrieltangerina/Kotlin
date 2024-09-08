package com.example.bancodigital.presenter.features.transfer.transfer_user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.domain.profile.GetProfilesUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferUserViewModel @Inject constructor(
    private val getProfilesUseCase: GetProfilesUseCase
) : ViewModel() {

    fun getProfilesUseCase() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val profiles = getProfilesUseCase.invoke()

            emit(StateView.Success(profiles))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}
package com.example.movieapp.presenter.main.bottombar.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.api.usecase.auth.LogoutUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _profileState = MutableLiveData<StateView<Unit>>()
    val profileState: LiveData<StateView<Unit>> get() = _profileState

    fun logout() {
        viewModelScope.launch {
            try {

                _profileState.postValue(StateView.Loading())

                logoutUseCase.invoke()

                _profileState.postValue(StateView.Success(Unit))

            } catch (e: Exception) {
                e.printStackTrace()
                _profileState.postValue(StateView.Error(e.message))
            }
        }
    }

}
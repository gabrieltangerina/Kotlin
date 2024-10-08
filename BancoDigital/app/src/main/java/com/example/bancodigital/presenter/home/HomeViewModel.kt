package com.example.bancodigital.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.domain.profile.GetProfileUseCase
import com.example.bancodigital.domain.transaction.GetTransactionsUseCase
import com.example.bancodigital.domain.wallet.GetBalanceUseCase
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val getBalanceUseCase: GetBalanceUseCase
) : ViewModel() {

    fun getBalance() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val balance = getBalanceUseCase.invoke()

            emit(StateView.Success(balance))

        }catch (ex: Exception){
            emit(StateView.Error(ex.message))
        }
    }

    fun getProfileUseCase() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val profile = getProfileUseCase.invoke(FirebaseHelper.getUserId())

            emit(StateView.Success(profile))

        }catch (ex: Exception){
            emit(StateView.Error(ex.message))
        }
    }

    fun getTransactions() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val transactions = getTransactionsUseCase.invoke()

            emit(StateView.Success(transactions))

        }catch (ex: Exception){
            emit(StateView.Error(ex.message))
        }
    }


}
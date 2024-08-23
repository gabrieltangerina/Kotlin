package com.example.bancodigital.presenter.features.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.domain.deposit.SaveDepositUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DepositViewModel @Inject constructor(
    private val saveDepositUseCase: SaveDepositUseCase
) : ViewModel(){

    fun saveDeposit(deposit: Deposit) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val depositId = saveDepositUseCase.invoke(deposit)

            emit(StateView.Sucess(depositId))

        }catch (ex: Exception){
            emit(StateView.Error(ex.message))
        }
    }

}
package com.example.bancodigital.presenter.features.transfer.transfer_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.domain.wallet.GetBalanceUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TransferFormViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase
) : ViewModel() {

    fun getBalance() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val balance = getBalanceUseCase.invoke()

            emit(StateView.Success(balance))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

}
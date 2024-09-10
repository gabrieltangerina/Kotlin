package com.example.bancodigital.presenter.features.transfer.transfer_confirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.domain.transaction.SaveTransactionUseCase
import com.example.bancodigital.domain.transfer.SaveTransferTransactionUseCase
import com.example.bancodigital.domain.transfer.SaveTransferUseCase
import com.example.bancodigital.domain.transfer.UpdateDateTransferTransactionUseCase
import com.example.bancodigital.domain.transfer.UpdateTransferUseCase
import com.example.bancodigital.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ConfirmTransferViewModel @Inject constructor(
    private val saveTransferUseCase: SaveTransferUseCase,
    private val updateTransferUseCase: UpdateTransferUseCase,
    private val saveTransferTransactionUseCase: SaveTransferTransactionUseCase,
    private val updateDateTransferTransactionUseCase: UpdateDateTransferTransactionUseCase
) : ViewModel() {

    fun saveTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveTransferUseCase.invoke(transfer)

            emit(StateView.Success(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun updateTransfer(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            updateTransferUseCase.invoke(transfer)

            emit(StateView.Success(Unit))

        } catch (ex: Exception) {
            emit(StateView.Error(ex.message))
        }
    }

    fun saveTransferTransaction(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            saveTransferTransactionUseCase.invoke(transfer)

            emit(StateView.Success(Unit))

        }catch (ex: Exception){
            emit(StateView.Error(ex.message))
        }
    }

    fun updateDateTransferTransaction(transfer: Transfer) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            updateDateTransferTransactionUseCase.invoke(transfer)

            emit(StateView.Success(Unit))

        }catch (ex: Exception){
            emit(StateView.Error(ex.message))
        }
    }

}
package com.example.bancodigital.data.repository.transfer

import com.example.bancodigital.data.model.Transfer

interface TransferDataSource {

    suspend fun saveTransfer(transfer: Transfer)

}
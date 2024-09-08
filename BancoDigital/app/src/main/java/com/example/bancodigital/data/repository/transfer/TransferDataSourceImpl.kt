package com.example.bancodigital.data.repository.transfer

import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransferDataSourceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : TransferDataSource {

    private val transferReference = firebaseDatabase.reference
        .child("transfer")

    override suspend fun saveTransfer(transfer: Transfer) {
        suspendCoroutine { continuation ->

            // Salva a transferencia para o usuário que fez a transferencia
            transferReference
                .child(transfer.idUserSent)
                .child(transfer.id)
                .setValue(transfer).addOnCompleteListener { taskUserSent ->
                    if(taskUserSent.isSuccessful){

                        // Salva a transferencia para o usuário que recebeu a transferencia
                        transferReference
                            .child(transfer.idUserReceipt)
                            .child(transfer.id)
                            .setValue(transfer).addOnCompleteListener { taskUserReceipt ->
                                if (taskUserReceipt.isSuccessful){
                                    continuation.resumeWith(Result.success(Unit))
                                }else{
                                    taskUserReceipt.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }
                    }else{
                        taskUserSent.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

}
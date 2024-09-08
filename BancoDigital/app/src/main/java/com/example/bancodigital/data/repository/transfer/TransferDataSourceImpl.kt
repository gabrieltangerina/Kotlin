package com.example.bancodigital.data.repository.transfer

import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
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
                    if (taskUserSent.isSuccessful) {

                        // Salva a transferencia para o usuário que recebeu a transferencia
                        transferReference
                            .child(transfer.idUserReceipt)
                            .child(transfer.id)
                            .setValue(transfer).addOnCompleteListener { taskUserReceipt ->
                                if (taskUserReceipt.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskUserReceipt.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }
                    } else {
                        taskUserSent.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun updateTransfer(transfer: Transfer) {
        suspendCoroutine { continuation ->

            // Salva a transferencia para o usuário que fez a transferencia
            transferReference
                .child(transfer.idUserSent)
                .child(transfer.id)
                .child("date")
                .setValue(ServerValue.TIMESTAMP)
                .addOnCompleteListener { taskUserSent ->
                    if (taskUserSent.isSuccessful) {

                        // Salva a data para o usuário que recebeu a transferencia
                        transferReference
                            .child(transfer.idUserReceipt)
                            .child(transfer.id)
                            .child("date")
                            .setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener { taskUserReceipt ->
                                if (taskUserReceipt.isSuccessful) {
                                    continuation.resumeWith(Result.success(Unit))
                                } else {
                                    taskUserReceipt.exception?.let {
                                        continuation.resumeWith(Result.failure(it))
                                    }
                                }
                            }

                    } else {
                        taskUserSent.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

    override suspend fun getTransfer(id: String): Transfer {
        return suspendCoroutine { continuation ->
            transferReference
                .child(FirebaseHelper.getUserId())
                .child(id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val transfer = snapshot.getValue(Transfer::class.java)

                        transfer?.let {
                            continuation.resumeWith(Result.success(transfer))
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }

                })
        }
    }

}
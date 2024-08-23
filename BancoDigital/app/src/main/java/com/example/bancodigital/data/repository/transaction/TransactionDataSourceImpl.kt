package com.example.bancodigital.data.repository.transaction

import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.util.FirebaseHelper
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class TransactionDataSourceImpl @Inject constructor(
    firebaseDatabase: FirebaseDatabase
) : TransactionDataSource {

    private val transactionReference = firebaseDatabase.reference
        .child("transaction")
        .child(FirebaseHelper.getUserId())

    override suspend fun saveTransaction(transaction: Transaction) {
        return suspendCoroutine { continuation ->
            transactionReference.child(transaction.id).setValue(transaction)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val dateReference = transactionReference
                            .child(transaction.id)
                            .child("date")

                        dateReference.setValue(ServerValue.TIMESTAMP)

                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        task.exception?.let {
                            continuation.resumeWith(Result.failure(it))
                        }
                    }
                }
        }
    }

}